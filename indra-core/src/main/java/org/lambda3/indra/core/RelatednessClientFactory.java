package org.lambda3.indra.core;

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

import org.lambda3.indra.core.exception.IndraError;
import org.lambda3.indra.core.impl.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class RelatednessClientFactory {
    private final Map<String, VectorSpace> vectorSpaces = new ConcurrentHashMap<>();
    private final Map<Params, RelatednessClient> clients = new ConcurrentHashMap<>();
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
                return clients.computeIfAbsent(params, (p) -> new CosineClient(p, vectorSpace));
            case SPEARMAN:
                return clients.computeIfAbsent(params, (p) -> new SpearmanClient(p, vectorSpace));
            case EUCLIDEAN:
                return clients.computeIfAbsent(params, (p) -> new EuclideanClient(p, vectorSpace));
            case JACCARD:
                return clients.computeIfAbsent(params, (p) -> new JaccardClient(p, vectorSpace));
            case PEARSON:
                return clients.computeIfAbsent(params, (p) -> new PearsonClient(p, vectorSpace));
            case ALPHASKEW:
                return clients.computeIfAbsent(params, (p) -> new AlphaSkewClient(p, vectorSpace));
            case CHEBYSHEV:
                return clients.computeIfAbsent(params, (p) -> new ChebyshevClient(p, vectorSpace));
            case CITYBLOCK:
                return clients.computeIfAbsent(params, (p) -> new CityBlockClient(p, vectorSpace));
            case DICE:
                return clients.computeIfAbsent(params, (p) -> new DiceClient(p, vectorSpace));
            case JACCARD2:
                return clients.computeIfAbsent(params, (p) -> new Jaccard2Client(p, vectorSpace));
            case JENSENSHANNON:
                return clients.computeIfAbsent(params, (p) -> new JensenShannonClient(p, vectorSpace));
            default:
                throw new IndraError("Unsupported Score Function.");
        }
    }

}
