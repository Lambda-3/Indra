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

import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.VectorSpaceFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RelatednessClientFactory {
    private final Map<String, RelatednessClient> clients = new ConcurrentHashMap<>();
    private VectorSpaceFactory vectorSpaceFactory;

    public RelatednessClientFactory(VectorSpaceFactory vectorSpaceFactory) {
        if (vectorSpaceFactory == null) {
            throw new IllegalArgumentException("vectorSpaceFactory is mandatory");
        }
        this.vectorSpaceFactory = vectorSpaceFactory;
    }

    public RelatednessClient create(Params params) {
        switch (params.func) {
            case COSINE:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new CosineClient(params, vectorSpaceFactory.create(params)));
            case CORRELATION:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new CorrelationClient(params, vectorSpaceFactory.create(params)));
            case EUCLIDEAN:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new EuclideanClient(params, vectorSpaceFactory.create(params)));
            case JACCARD:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new JaccardClient(params, vectorSpaceFactory.create(params)));
            case LIN:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new LinClient(params, vectorSpaceFactory.create(params)));
            case TANIMOTO:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new TanimotoClient(params, vectorSpaceFactory.create(params)));
            case ALPHASKEW:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new AlphaSkewClient(params, vectorSpaceFactory.create(params)));
            case CHEBYSHEV:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new ChebyshevClient(params, vectorSpaceFactory.create(params)));
            case CITYBLOCK:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new CityBlockClient(params, vectorSpaceFactory.create(params)));
            case DICE:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new DiceClient(params, vectorSpaceFactory.create(params)));
            case JACCARD2:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new Jaccard2Client(params, vectorSpaceFactory.create(params)));
            case JENSENSHANNON:
                return clients.computeIfAbsent(params.getDBName(), (dbName) ->
                        new JensenShannonClient(params, vectorSpaceFactory.create(params)));
            default:
                throw new RuntimeException("Unsupported Score Function.");
        }
    }

}
