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

import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.ScoredTextPair;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.core.VectorSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class RelatednessBaseClient extends RelatednessClient {
    private VectorSpace vectorSpace;
    private Params params;

    RelatednessBaseClient(Params params, VectorSpace vectorSpace) {
        if (params == null || vectorSpace == null) {
            throw new IllegalArgumentException("Missing required arguments.");
        }
        this.vectorSpace = vectorSpace;
        this.params = params;

    }

    protected abstract int getVectorSizeLimit();

    protected abstract double sim(double[] v1, double[] v2);

    @Override
    protected Params getParams() {
        return params;
    }

    @Override
    protected List<ScoredTextPair> compute(List<AnalyzedPair> pairs) {
        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectors(pairs, getVectorSizeLimit());

        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        vectorPairs.entrySet().stream().forEach(e -> {
            AnalyzedPair pair = e.getKey();
            VectorPair vectorPair = e.getValue();

            if (vectorPair.v1 != null && vectorPair.v2 != null) {
                double[] v1 = vectorPair.v1.values().stream().mapToDouble(d -> d).toArray();
                double[] v2 = vectorPair.v2.values().stream().mapToDouble(d -> d).toArray();
                scoredTextPairs.add(new ScoredTextPair(pair, sim(v1, v2)));
            }
            else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }

        });

        return scoredTextPairs;
    }
}
