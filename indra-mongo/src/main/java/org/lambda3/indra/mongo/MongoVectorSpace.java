package org.lambda3.indra.mongo;

/*-
 * ==========================License-Start=============================
 * Indra Mongo Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.bson.Document;
import org.bson.types.Binary;
import org.lambda3.indra.core.CachedVectorSpace;
import org.lambda3.indra.core.composition.VectorComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

class MongoVectorSpace extends CachedVectorSpace {

    private static int MAXSDIMENSIONS = 5000000;
    private static final String TERM_FIELD_NAME = "term";
    private static final String VECTOR_FIELD_NAME = "vector";
    private static final String TERMS_COLL_NAME = "terms";

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, RealVector> vectorsCache = new ConcurrentHashMap<>();
    private MongoClient mongoClient;
    private final String dbName;

    MongoVectorSpace(MongoClient client, String dbName, VectorComposer composer, VectorComposer translationComposer) {
        super(composer, translationComposer);
        logger.info("Creating new vector space from {}", dbName);
        this.mongoClient = client;
        this.dbName = dbName;
    }

    @Override
    public boolean isSparse() {
        return dbName.toLowerCase().startsWith("esa-");
    }

    @Override
    public int getVectorSize() {
        return 1500;
    }


    private MongoCollection<Document> getColl() {
        return this.mongoClient.getDatabase(dbName).getCollection(TERMS_COLL_NAME);
    }

    @Override
    protected void collectVectors(Collection<String> terms, int limit) {
        Set<String> toFetch = terms.stream()
                .filter(t -> !this.vectorsCache.containsKey(t))
                .collect(Collectors.toSet());

        logger.debug("Cache has {} vectors, need to fetch more {}",
                terms.size() - toFetch.size(), toFetch.size());

        if (!toFetch.isEmpty()) {
            logger.info("Collecting {} term vectors from {}", toFetch.size(), dbName);
            FindIterable<Document> docs = getColl().find(Filters.in(TERM_FIELD_NAME, toFetch));
            if (docs != null) {
                docs.batchSize(toFetch.size());
                for (Document doc : docs) {
                    this.vectorsCache.put(doc.getString(TERM_FIELD_NAME), unmarshall(doc, limit));
                }
            }
        }
    }

    @Override
    protected List<RealVector> getFromCache(Collection<String> terms) {
        List<RealVector> termVectors = new ArrayList<>();
        terms.stream().
                filter(t -> this.vectorsCache.containsKey(t)).
                forEach((t) -> termVectors.add(this.vectorsCache.get(t)));
        return termVectors;
    }

    private RealVector unmarshall(Document doc, int limit) {
        final Binary binary = doc.get(VECTOR_FIELD_NAME, Binary.class);
        final byte[] b = binary.getData();

        try (DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b))) {
            int key;
            double score;
            int size = Math.min(dis.readInt(), limit);

            RealVector vector;
            if (isSparse()) {
                vector = new OpenMapRealVector(MAXSDIMENSIONS);
            } else {
                vector = new ArrayRealVector(size);
            }

            for (int i = 0; i < size; i++) {
                key = dis.readInt();
                score = dis.readFloat(); //TODO: models were inserted as float. In the next generation version, insert by double.
                vector.setEntry(key, score);
            }

            return vector;
        } catch (IOException e) {
            logger.error("Fail to read vector data.", e);
        }
        return null;
    }
}
