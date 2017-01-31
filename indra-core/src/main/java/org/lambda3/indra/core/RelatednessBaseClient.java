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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.ScoredTextPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RelatednessBaseClient extends RelatednessClient {
    private static int MAXSDIMENSIONS = 5000000;
    private VectorSpace vectorSpace;
    private Params params;

    protected RelatednessBaseClient(Params params, VectorSpace vectorSpace) {
        if (params == null || vectorSpace == null) {
            throw new IllegalArgumentException("Missing required arguments.");
        }
        this.vectorSpace = vectorSpace;
        this.params = params;

    }

    protected abstract double sim(RealVector r1, RealVector r2, boolean sparse);

    @Override
    protected Params getParams() {
        return params;
    }

    @Override
    protected List<ScoredTextPair> compute(List<AnalyzedPair> pairs) {
        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectors(pairs);

        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        vectorPairs.entrySet().forEach(e -> {
            AnalyzedPair pair = e.getKey();
            VectorPair vectorPair = e.getValue();
            if (vectorPair.v1 != null && vectorPair.v2 != null) {

                if (!vectorSpace.isSparse()) {
                    double[] v1 = vectorPair.v1.values().stream().mapToDouble(d -> d).toArray();
                    double[] v2 = vectorPair.v2.values().stream().mapToDouble(d -> d).toArray();
                    scoredTextPairs.add(new ScoredTextPair(pair,
                            sim(new ArrayRealVector(v1), new ArrayRealVector(v2), false)));
                }
                else {
                    //TODO: Fix me!
                    // Currently only ESA is sparse.
                    // This max value comes from the number of wikipedia articles for english, the largest corpus.
                    OpenMapRealVector r1 = new OpenMapRealVector(MAXSDIMENSIONS);
                    OpenMapRealVector r2 = new OpenMapRealVector(MAXSDIMENSIONS);
                    vectorPair.v1.forEach(r1::setEntry);
                    vectorPair.v2.forEach(r2::setEntry);
                    scoredTextPairs.add(new ScoredTextPair(pair, sim(r1, r2, true)));
                }

            } else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }

        });

        return scoredTextPairs;
    }
}
