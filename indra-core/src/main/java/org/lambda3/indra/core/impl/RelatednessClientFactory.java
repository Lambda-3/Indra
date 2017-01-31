package org.lambda3.indra.core.impl;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

import com.mongodb.MongoClientURI;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RelatednessClientFactory {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final MongoClientURI mURI;
    private final Map<String, RelatednessClient> clients = new ConcurrentHashMap<>();


    public RelatednessClientFactory(String mongoURI) {
        if (mongoURI == null || mongoURI.isEmpty()) {
            throw new IllegalArgumentException("mongoURI is mandatory");
        }
        this.mURI = new MongoClientURI(mongoURI);
        logger.debug("Factory initialized.");
    }

    public RelatednessClient create(Params params) {
        switch (params.func) {
            case COSINE:
                return getCosineClient(params);
            case CORRELATION:
                return getCorrelationClient(params);
            case EUCLIDEAN:
                return getEuclideanClient(params);
            case JACCARD:
                return getJaccardClient(params);
            case LIN:
                return getLinClient(params);
            case TANIMOTO:
                return getTanimotoClient(params);
            case ALPHASKEW:
                return getAlphaSkewClient(params);
            case CHEBYSHEV:
                return getChebyshevClient(params);
            case CITYBLOCK:
                return getCityBlockClient(params);
            case DICE:
                return getDiceClient(params);
            case JACCARD2:
                return getJaccard2Client(params);
            case JENSENSHANNON:
                return getJensenShannonClient(params);
            default:
                throw new RuntimeException("Unsupported Score Function.");
        }
    }

    //VectorSpace implements a cache and is thread-safe, should we share between clients?

    private RelatednessClient getCosineClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new CosineClient(params, vectorSpace);
        });
    }

    private RelatednessClient getCorrelationClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new CorrelationClient(params, vectorSpace);
        });
    }

    private RelatednessClient getEuclideanClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new EuclideanClient(params, vectorSpace);
        });
    }

    private RelatednessClient getJaccardClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new JaccardClient(params, vectorSpace);
        });
    }

    private RelatednessClient getLinClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new LinClient(params, vectorSpace);
        });
    }

    private RelatednessClient getTanimotoClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new TanimotoClient(params, vectorSpace);
        });
    }

    private RelatednessClient getAlphaSkewClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new AlphaSkewClient(params, vectorSpace);
        });
    }

    private RelatednessClient getChebyshevClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new ChebyshevClient(params, vectorSpace);
        });
    }

    private RelatednessClient getCityBlockClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new CityBlockClient(params, vectorSpace);
        });
    }

    private RelatednessClient getDiceClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new DiceClient(params, vectorSpace);
        });
    }

    private RelatednessClient getJaccard2Client(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (dbName) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, dbName);
            return new Jaccard2Client(params, vectorSpace);
        });
    }

    private RelatednessClient getJensenShannonClient(final Params params) {
        return clients.computeIfAbsent(params.getDBName(), (p) -> {
            MongoVectorSpace vectorSpace = new MongoVectorSpace(mURI, p);
            return new JensenShannonClient(params, vectorSpace);
        });
    }

}
