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

import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.function.RelatednessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class RelatednessClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected VectorSpace vectorSpace;
    protected Params params;
    protected RelatednessFunction func;

    protected RelatednessClient(Params params, VectorSpace vectorSpace, RelatednessFunction func) {
        if (params == null || vectorSpace == null || func == null) {
            throw new IllegalArgumentException("Missing required arguments.");
        }
        this.vectorSpace = vectorSpace;
        this.params = params;
        this.func = func;
    }

    protected abstract List<? extends AnalyzedPair> doAnalyze(List<TextPair> pairs);

    protected abstract Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs);

    protected List<ScoredTextPair> compute(Map<? extends AnalyzedPair, VectorPair> vectorPairs) {
        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            VectorPair vectorPair = vectorPairs.get(pair);

            if (vectorPair.v1 != null && vectorPair.v2 != null) {

                if (!vectorSpace.getMetadata().isSparse()) {
                    scoredTextPairs.add(new ScoredTextPair(pair,
                            func.sim(vectorPair.v1, vectorPair.v2, false)));
                } else {
                    scoredTextPairs.add(new ScoredTextPair(pair,
                            func.sim(vectorPair.v1, vectorPair.v2, true)));
                }

            } else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }

        }

        return scoredTextPairs;
    }

    public final RelatednessResult getRelatedness(List<TextPair> pairs) {
        List<? extends AnalyzedPair> analyzedPairs = doAnalyze(pairs);
        Map<? extends AnalyzedPair, VectorPair> vectorsPairs = getVectors(analyzedPairs);
        return new RelatednessResult(compute(vectorsPairs));
    }
}
