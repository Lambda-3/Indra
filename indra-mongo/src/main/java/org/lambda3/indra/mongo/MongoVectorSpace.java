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
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.bson.Document;
import org.bson.types.Binary;
import org.lambda3.indra.client.ModelMetadata;
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

public class MongoVectorSpace extends CachedVectorSpace {

    private static final int MAX_DIMENSIONS = 5000000;

    private static final String TERM_FIELD_NAME = "term";
    private static final String VECTOR_FIELD_NAME = "vector";

    private static final String TERMS_COLL_NAME = "terms";
    public static final String METADATA_COLL_NAME = "metadata";

    private Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String, RealVector> vectorsCache = new ConcurrentHashMap<>();
    private MongoClient mongoClient;
    private final String dbName;
    private ModelMetadata metadata;

    MongoVectorSpace(MongoClient client, String dbName, VectorComposer composer, VectorComposer translationComposer) {
        super(composer, translationComposer);
        logger.info("Creating new vector space from {}", dbName);
        this.mongoClient = client;
        this.dbName = dbName;
        configure();
    }

    private void configure() {
        MongoDatabase db = this.mongoClient.getDatabase(dbName);
        boolean containsMetadataCollection = false;
        for (String collName : db.listCollectionNames()) {
            if (collName.equalsIgnoreCase(METADATA_COLL_NAME)) {
                containsMetadataCollection = true;
                break;
            }
        }

        this.metadata = ModelMetadata.createDefault();

        if (containsMetadataCollection) {
            MongoCollection<Document> metadataColl = db.getCollection(METADATA_COLL_NAME);
            FindIterable<Document> metadataDocs = metadataColl.find();

            for (Document doc : metadataDocs) {

                Object loaderId = doc.get(ModelMetadata.LOADER_ID_PARAM);
                if (loaderId != null) {
                    this.metadata.loaderId((String) loaderId);
                }

                Object sparse = doc.get(ModelMetadata.SPARSE_PARAM);
                if (sparse != null) {
                    this.metadata.sparse((Boolean) sparse);
                }

                Object binary = doc.get(ModelMetadata.BINARY_PARAM);
                if (binary != null) {
                    this.metadata.binary((Boolean) binary);
                }

                Object applyStemmer = doc.get(ModelMetadata.APPLY_STEMMER_PARAM);
                if (applyStemmer != null) {
                    this.metadata.applyStemmer((Integer) applyStemmer);
                }

                Object removeAccents = doc.get(ModelMetadata.REMOVE_ACCENTS_PARAM);
                if (removeAccents != null) {
                    this.metadata.removeAccents((Boolean) removeAccents);
                }

                Object applyLowercase = doc.get(ModelMetadata.APPLY_LOWERCASE_PARAM);
                if (applyLowercase != null) {
                    this.metadata.applyLowercase((Boolean) applyLowercase);
                }

                Object applyStopWords = doc.get(ModelMetadata.APPLY_STOP_WORDS_PARAM);
                if (applyStopWords != null) {
                    this.metadata.applyStopWords((Boolean) applyStopWords);
                }

                Object minWordLength = doc.get(ModelMetadata.MIN_WORD_LENGTH_PARAM);
                if (minWordLength != null) {
                    this.metadata.minWordLength((Integer) minWordLength);
                }

                Object maxWordLength = doc.get(ModelMetadata.MAX_WORD_LENGTH_PARAM);
                if (maxWordLength != null) {
                    this.metadata.maxWordLength((Integer) maxWordLength);
                }

                Object dimensions = doc.get(ModelMetadata.DIMENSIONS_PARAM);
                if (dimensions != null) {
                    this.metadata.dimensions((Integer) dimensions);
                }

                Object stopWords = doc.get(ModelMetadata.STOP_WORDS_PARAM);
                if (stopWords != null) {
                    Set<String> effectiveStopWords = new HashSet<>();
                    effectiveStopWords.addAll((Collection<? extends String>) stopWords);
                    this.metadata.stopWords(effectiveStopWords);
                }

                break;
            }
        }
    }

    @Override
    public ModelMetadata getMetadata() {
        return metadata;
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
            if (getMetadata().isSparse()) {
                vector = new OpenMapRealVector(MAX_DIMENSIONS);
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
