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

import org.lambda3.indra.common.client.ScoreFunction;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.VectorSpace;
import org.lambda3.indra.core.VectorSpaceFactory;
import org.lambda3.indra.core.exception.IndraError;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RelatednessClientFactory {
    private final Map<String, VectorSpace> vectorSpaces = new ConcurrentHashMap<>();
    private final Map<ScoreFunction, RelatednessClient> clients = new ConcurrentHashMap<>();
    private VectorSpaceFactory vectorSpaceFactory;

    public RelatednessClientFactory(VectorSpaceFactory vectorSpaceFactory) {
        if (vectorSpaceFactory == null) {
            throw new IllegalArgumentException("vectorSpaceFactory is mandatory");
        }
        this.vectorSpaceFactory = vectorSpaceFactory;
    }

    public RelatednessClient create(Params params) {
        VectorSpace vectorSpace = vectorSpaces.computeIfAbsent(params.getDBName(),
                (String) -> vectorSpaceFactory.create(params));

        switch (params.func) {
            case COSINE:
                return clients.computeIfAbsent(params.func, (String) -> new CosineClient(params, vectorSpace));
            case SPEARMAN:
                return clients.computeIfAbsent(params.func, (String) -> new SpearmansClient(params, vectorSpace));
            case EUCLIDEAN:
                return clients.computeIfAbsent(params.func, (String) -> new EuclideanClient(params, vectorSpace));
            case JACCARD:
                return clients.computeIfAbsent(params.func, (String) -> new JaccardClient(params, vectorSpace));
            case PEARSON:
                return clients.computeIfAbsent(params.func, (String) -> new PearsonClient(params, vectorSpace));
            case ALPHASKEW:
                return clients.computeIfAbsent(params.func, (String) -> new AlphaSkewClient(params, vectorSpace));
            case CHEBYSHEV:
                return clients.computeIfAbsent(params.func, (String) -> new ChebyshevClient(params, vectorSpace));
            case CITYBLOCK:
                return clients.computeIfAbsent(params.func, (String) -> new CityBlockClient(params, vectorSpace));
            case DICE:
                return clients.computeIfAbsent(params.func, (String) -> new DiceClient(params, vectorSpace));
            case JACCARD2:
                return clients.computeIfAbsent(params.func, (String) -> new Jaccard2Client(params, vectorSpace));
            case JENSENSHANNON:
                return clients.computeIfAbsent(params.func, (String) -> new JensenShannonClient(params, vectorSpace));
            default:
                throw new IndraError("Unsupported Score Function.");
        }
    }

}
