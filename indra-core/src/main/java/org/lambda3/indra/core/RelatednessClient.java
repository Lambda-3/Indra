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
import org.lambda3.indra.client.*;
import org.lambda3.indra.core.function.RelatednessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class RelatednessClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected VectorSpace vectorSpace;
    protected RelatednessRequest request;
    protected RelatednessFunction func;

    protected RelatednessClient(RelatednessRequest request, VectorSpace vectorSpace, RelatednessFunction func) {
        this.vectorSpace = Objects.requireNonNull(vectorSpace);
        this.request = Objects.requireNonNull(request);
        this.func = Objects.requireNonNull(func);
    }

    protected abstract List<AnalyzedPair> doAnalyze(List<TextPair> pairs);

    protected abstract Map<AnalyzedTerm, List<AnalyzedTerm>> doAnalyze(String one, List<String> many);

    protected abstract Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs);

    protected abstract Map<AnalyzedTerm, RealVector> getVectors(Map<AnalyzedTerm, List<AnalyzedTerm>> analyzedTerms);

    protected List<ScoredTextPair> compute(Map<? extends AnalyzedPair, VectorPair> vectorPairs) {
        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            VectorPair vectorPair = vectorPairs.get(pair);

            if (vectorPair.v1 != null && vectorPair.v2 != null) {
                scoredTextPairs.add(new ScoredTextPair(pair,
                        func.sim(vectorPair.v1, vectorPair.v2, vectorSpace.getMetadata().isSparse())));
            } else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }
        }

        return scoredTextPairs;
    }

    public final List<ScoredTextPair> getRelatedness(List<TextPair> pairs) {
        List<AnalyzedPair> analyzedPairs = doAnalyze(pairs);
        Map<? extends AnalyzedPair, VectorPair> vectorsPairs = getVectors(analyzedPairs);
        return compute(vectorsPairs);
    }

    public Map<String, Double> getRelatedness(String one, List<String> many) {
        Map<AnalyzedTerm, List<AnalyzedTerm>> analyzedTerms = doAnalyze(one, many);
        Map<AnalyzedTerm, RealVector> vectors = getVectors(analyzedTerms);

        Map<String, Double> results = new LinkedHashMap<>();

        for (AnalyzedTerm oneAnalyzed : analyzedTerms.keySet()) {
            RealVector oneVector = vectors.get(oneAnalyzed);

            if (oneVector != null) {
                List<AnalyzedTerm> manyTerms = analyzedTerms.get(oneAnalyzed);

                for (AnalyzedTerm mTerm : manyTerms) {
                    RealVector mVector = vectors.get(mTerm);
                    if (mVector != null) {
                        double score = func.sim(oneVector, mVector, vectorSpace.getMetadata().isSparse());
                        results.put(mTerm.getTerm(), score);
                    } else {
                        results.put(mTerm.getTerm(), 0d);
                    }
                }
            }
        }

        return results;
    }
}
