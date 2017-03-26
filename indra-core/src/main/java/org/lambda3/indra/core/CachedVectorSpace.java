package org.lambda3.indra.core;

import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.MutableAnalyzedTerm;
import org.lambda3.indra.core.utils.VectorsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
public abstract class CachedVectorSpace implements VectorSpace {

    private Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract void collectVectors(Collection<String> terms, int limit);

    protected abstract List<Map<Integer, Double>> getFromCache(Set<String> terms);

    @Override
    public Map<AnalyzedPair, VectorPair> getVectorPairs(List<AnalyzedPair> pairs) {
        if (pairs == null) {
            throw new IllegalArgumentException("pairs can't be null");
        }

        Map<AnalyzedPair, VectorPair> res = new ConcurrentHashMap<>();

        Set<String> allTerms = new HashSet<>();
        for (AnalyzedPair p : pairs) {
            allTerms.addAll(p.getAnalyzedT1().getStemmedTargetTokens());
            allTerms.addAll(p.getAnalyzedT2().getStemmedTargetTokens());
        }

        collectVectors(allTerms, getVectorSize());

        for (AnalyzedPair p : pairs) {
            VectorPair vectorPair = new VectorPair();
            vectorPair.v1 = composeVectors(p.getAnalyzedT1().getStemmedTargetTokens());
            vectorPair.v2 = composeVectors(p.getAnalyzedT2().getStemmedTargetTokens());
            res.put(p, vectorPair);
        }

        return res;
    }

    @Override
    public Map<MutableAnalyzedTerm, Map<Integer, Double>> getVectors(List<MutableAnalyzedTerm> terms) {
        if (terms == null) {
            throw new IllegalArgumentException("terms can't be null");
        }

        Set<String> allTerms = new HashSet<>();
        terms.forEach(t -> allTerms.addAll(t.getStemmedTargetTokens()));

        collectVectors(allTerms, getVectorSize());

        Map<MutableAnalyzedTerm, Map<Integer, Double>> vectors = new HashMap<>();

        for (MutableAnalyzedTerm term : terms) {
            Map<Integer, Double> vector = composeVectors(term.getStemmedTargetTokens());
            vectors.put(term, vector);
        }

        return vectors;
    }

    //TODO: Decouple this to use different methods of vector composition
    private Map<Integer, Double> composeVectors(List<String> terms) {
        logger.trace("Composing {} vectors", terms.size());
        List<Map<Integer, Double>> vectors = getFromCache(new HashSet<>(terms));
        if (!vectors.isEmpty()) {
            return VectorsUtils.add(vectors);
        }

        return null;
    }
}
