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
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.apache.commons.math3.linear.RealVector;
import org.bson.Document;
import org.bson.types.Binary;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;
import org.lambda3.indra.core.CachedVectorSpace;
import org.lambda3.indra.core.codecs.BinaryCodecs;
import org.lambda3.indra.core.exception.IndraError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MongoVectorSpace extends CachedVectorSpace {
    private static final String TERM_FIELD_NAME = "term";
    private static final String VECTOR_FIELD_NAME = "vector";
    private static final String TERMS_COLL_NAME = "terms";
    private static final String METADATA_COLL_NAME = "metadata";

    private Logger logger = LoggerFactory.getLogger(getClass());
    private MongoClient mongoClient;
    private final String dbName;

    MongoVectorSpace(MongoClient client, String dbName) {
        logger.info("Creating new vector space from {}", dbName);
        this.mongoClient = client;
        this.dbName = dbName;
    }

    @Override
    protected ModelMetadata loadMetadata() {
        MongoDatabase db = this.mongoClient.getDatabase(dbName);
        MongoCollection<Document> metadataColl = db.getCollection(METADATA_COLL_NAME);

        if (metadataColl.count() > 1) {
            throw new IndraError("Model metadata must have only one entry!");
        }

        if (metadataColl.count() == 1) {
            logger.debug("Using stored metadata of {}", dbName);
            Document storedMetadata = metadataColl.find().first();
            return ModelMetadata.createFromMap(storedMetadata);
        } else {
            logger.debug("No metadata found in {}, using defaults.", dbName);
            return ModelMetadata.createDefault();
        }
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk) {
        throw new UnsupportedOperationException("Mongo implementation does not support 'nearest' function.");
    }

    @Override
    public Collection<String> getNearestTerms(AnalyzedTerm term, int topk) {
        throw new UnsupportedOperationException("Mongo implementation does not support 'nearest' function.");
    }

    private MongoCollection<Document> getTermsColl() {
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
            FindIterable<Document> docs = getTermsColl().find(Filters.in(TERM_FIELD_NAME, toFetch));
            if (docs != null) {
                docs.batchSize(toFetch.size());
                for (Document doc : docs) {
                    this.vectorsCache.put(doc.getString(TERM_FIELD_NAME), unmarshall(doc, limit));
                }
            }
        }
    }

    private RealVector unmarshall(Document doc, int limit) {
        if (!metadata.isBinary()) {
            throw new UnsupportedOperationException("Can't consume non-binary models.");
        }

        try {
            final Binary binary = doc.get(VECTOR_FIELD_NAME, Binary.class);
            final byte[] b = binary.getData();

            if (metadata.getLoaderId().equalsIgnoreCase("legacy")) {
                return BinaryCodecs.legacyUnmarshall(b, limit, metadata.isSparse(), metadata.getDimensions());
            } else {
                return BinaryCodecs.unmarshall(b, metadata.isSparse(), metadata.getDimensions());
            }
        } catch (Exception e) {
            logger.error("Error unmarshalling vector", e);
        }

        return null;
    }
}
