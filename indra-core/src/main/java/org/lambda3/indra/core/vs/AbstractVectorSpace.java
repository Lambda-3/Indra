package org.lambda3.indra.core.vs;

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
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.entity.composition.VectorComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractVectorSpace implements VectorSpace {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected ModelMetadata metadata;

    protected abstract ModelMetadata loadMetadata();

    @Override
    public ModelMetadata getMetadata() {
        if (metadata == null) {
            metadata = loadMetadata();
        }

        return metadata;
    }

    @Override
    public Map<AnalyzedPair, VectorPair> getVectorPairs(List<AnalyzedPair> pairs, VectorComposer termComposer) {
        if (pairs == null || pairs.isEmpty()) {
            throw new IllegalArgumentException("pairs can't be null");
        }

        Map<AnalyzedPair, VectorPair> res = new HashMap<>();

        Set<String> allTerms = new HashSet<>();
        for (AnalyzedPair p : pairs) {
            allTerms.addAll(p.getAnalyzedT1().getAnalyzedTokens());
            allTerms.addAll(p.getAnalyzedT2().getAnalyzedTokens());
        }

        Map<String, RealVector> vectors = collectVectors(allTerms);

        for (AnalyzedPair p : pairs) {
            VectorPair vectorPair = new VectorPair();
            vectorPair.v1 = composeVectors(vectors, p.getAnalyzedT1().getAnalyzedTokens(), termComposer);
            vectorPair.v2 = composeVectors(vectors, p.getAnalyzedT2().getAnalyzedTokens(), termComposer);
            res.put(p, vectorPair);
        }
        return res;
    }

    @Override
    public Map<AnalyzedTranslatedPair, VectorPair> getTranslatedVectorPairs(List<AnalyzedTranslatedPair> pairs,
                                                                            VectorComposer termComposer, VectorComposer translationComposer) {
        if (pairs == null) {
            throw new IllegalArgumentException("pairs can't be null");
        }

        Map<AnalyzedTranslatedPair, VectorPair> res = new HashMap<>();

        Set<String> allTerms = new HashSet<>();
        for (AnalyzedTranslatedPair p : pairs) {
            MutableTranslatedTerm mtt1 = p.getTranslatedT1();
            for (String token : mtt1.getAnalyzedTranslatedTokens().keySet()) {
                allTerms.addAll(mtt1.getAnalyzedTranslatedTokens().get(token));
            }

            MutableTranslatedTerm mtt2 = p.getTranslatedT2();
            for (String token : mtt2.getAnalyzedTranslatedTokens().keySet()) {
                allTerms.addAll(mtt2.getAnalyzedTranslatedTokens().get(token));
            }
        }

        Map<String, RealVector> vectors = collectVectors(allTerms);

        for (AnalyzedTranslatedPair p : pairs) {
            VectorPair vectorPair = new VectorPair();

            List<RealVector> t1Vectors = new LinkedList<>();
            MutableTranslatedTerm tt1 = p.getTranslatedT1();
            for (String token : tt1.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(vectors, tt1.getAnalyzedTranslatedTokens().get(token), translationComposer);
                if (tokenVector != null) {
                    t1Vectors.add(tokenVector);
                }
            }

            List<RealVector> t2Vectors = new LinkedList<>();
            MutableTranslatedTerm tt2 = p.getTranslatedT2();
            for (String token : tt2.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(vectors, tt2.getAnalyzedTranslatedTokens().get(token), translationComposer);
                if (tokenVector != null) {
                    t2Vectors.add(tokenVector);
                }
            }

            vectorPair.v1 = t1Vectors.isEmpty() ? null : termComposer.compose(t1Vectors);
            vectorPair.v2 = t2Vectors.isEmpty() ? null : termComposer.compose(t2Vectors);
            res.put(p, vectorPair);
        }

        return res;
    }

    @Override
    public Map<String, RealVector> getVectors(List<AnalyzedTerm> terms, VectorComposer termComposer) {
        if (terms == null) {
            throw new IllegalArgumentException("terms can't be null");
        }

        Set<String> allTerms = new HashSet<>();
        terms.forEach(t -> allTerms.addAll(t.getAnalyzedTokens()));
        Map<String, RealVector> allVectors = collectVectors(allTerms);

        Map<String, RealVector> results = new HashMap<>();
        for (AnalyzedTerm term : terms) {
            RealVector vector = composeVectors(allVectors, term.getAnalyzedTokens(), termComposer);
            results.put(term.getTerm(), vector);
        }

        return results;
    }

    @Override
    public Map<String, RealVector> getTranslatedVectors(List<MutableTranslatedTerm> terms,
                                                        VectorComposer termComposer, VectorComposer translationComposer) {
        if (terms == null) {
            throw new IllegalArgumentException("terms can't be null");
        }

        Set<String> allTerms = new HashSet<>();

        for (MutableTranslatedTerm mtt : terms) {
            for (String token : mtt.getAnalyzedTranslatedTokens().keySet()) {
                allTerms.addAll(mtt.getAnalyzedTranslatedTokens().get(token));
            }
        }

        Map<String, RealVector> AllVectors = collectVectors(allTerms);

        Map<String, RealVector> results = new HashMap<>();

        for (MutableTranslatedTerm mtt : terms) {
            List<RealVector> tokenVectors = new LinkedList<>();
            for (String token : mtt.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(AllVectors, mtt.getAnalyzedTranslatedTokens().get(token), translationComposer);
                if (tokenVector != null) {
                    tokenVectors.add(tokenVector);
                }
            }

            RealVector vector = tokenVectors.isEmpty() ? null : termComposer.compose(tokenVectors);
            results.put(mtt.getTerm(), vector);
        }

        return results;
    }

    protected abstract Map<String, RealVector> collectVectors(Iterable<? extends String> terms);

    private RealVector composeVectors(Map<String, RealVector> allVectors, List<String> terms, VectorComposer composer) {
        logger.trace("Composing {} vectors", terms.size());

        List<RealVector> vectors = allVectors.entrySet().stream().filter(e -> terms.contains(e.getKey())).
                map(Map.Entry::getValue).collect(Collectors.toList());

        return composer.compose(vectors);
    }
}
