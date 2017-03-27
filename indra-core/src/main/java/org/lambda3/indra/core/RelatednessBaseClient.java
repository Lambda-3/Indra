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

import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.core.translation.Translator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RelatednessBaseClient extends RelatednessClient {
    private VectorSpace vectorSpace;
    private Params params;
    private Translator translator;

    protected RelatednessBaseClient(Params params, VectorSpace vectorSpace, Translator translator) {
        if (params == null || vectorSpace == null) {
            throw new IllegalArgumentException("Missing required arguments.");
        }

        if (params.translate && translator == null) {
            throw new IllegalArgumentException("Translate-based relatedness demands a translator.");
        }

        this.vectorSpace = vectorSpace;
        this.params = params;
        this.translator = translator;
    }

    protected abstract double sim(RealVector r1, RealVector r2, boolean sparse);

    @Override
    protected Params getParams() {
        return params;
    }

    @Override
    protected Translator getTranslator() {
        return this.translator;
    }

    @Override
    protected List<ScoredTextPair> compute(List<AnalyzedPair> pairs) {
        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectorPairs(pairs);

        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            VectorPair vectorPair = vectorPairs.get(pair);

            if (vectorPair.v1 != null && vectorPair.v2 != null) {

                if (!vectorSpace.isSparse()) {
                    scoredTextPairs.add(new ScoredTextPair(pair,
                            sim(vectorPair.v1, vectorPair.v2, false)));
                } else {
                    scoredTextPairs.add(new ScoredTextPair(pair,
                            sim(vectorPair.v1, vectorPair.v2, true)));
                }

            } else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }

        }

        return scoredTextPairs;
    }
}
