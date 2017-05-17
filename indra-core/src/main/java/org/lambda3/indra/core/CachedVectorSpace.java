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
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.AnalyzedTranslatedPair;
import org.lambda3.indra.client.MutableTranslatedTerm;
import org.lambda3.indra.core.composition.VectorComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public abstract class CachedVectorSpace implements VectorSpace {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private VectorComposer termComposer;
    private VectorComposer translationComposer;

    protected abstract void collectVectors(Collection<String> terms, int limit);

    protected abstract List<RealVector> getFromCache(Collection<String> terms);

    public CachedVectorSpace(VectorComposer termComposer, VectorComposer translationComposer) {
        this.termComposer = termComposer;
        this.translationComposer = translationComposer;
    }

    @Override
    public VectorComposer getTermComposer() {
        return this.termComposer;
    }

    @Override
    public VectorComposer getTranslationComposer() {
        return this.translationComposer;
    }

    @Override
    public Map<AnalyzedPair, VectorPair> getVectorPairs(List<AnalyzedPair> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            throw new IllegalArgumentException("pairs can't be null");
        }

        Map<AnalyzedPair, VectorPair> res = new HashMap<>();

        Set<String> allTerms = new HashSet<>();
        for (AnalyzedPair p : pairs) {
            allTerms.addAll(p.getAnalyzedT1().getAnalyzedTokens());
            allTerms.addAll(p.getAnalyzedT2().getAnalyzedTokens());
        }

        collectVectors(allTerms, getMetadata().getDimensions());

        for (AnalyzedPair p : pairs) {
            VectorPair vectorPair = new VectorPair();
            vectorPair.v1 = composeVectors(p.getAnalyzedT1().getAnalyzedTokens(), getTermComposer());
            vectorPair.v2 = composeVectors(p.getAnalyzedT2().getAnalyzedTokens(), getTermComposer());
            res.put(p, vectorPair);
        }
        return res;
    }

    @Override
    public Map<AnalyzedTranslatedPair, VectorPair> getTranslatedVectorPairs(List<AnalyzedTranslatedPair> pairs) {
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

        collectVectors(allTerms, getMetadata().getDimensions());


        for (AnalyzedTranslatedPair p : pairs) {
            VectorPair vectorPair = new VectorPair();

            List<RealVector> t1Vectors = new LinkedList<>();
            MutableTranslatedTerm tt1 = p.getTranslatedT1();
            for (String token : tt1.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(tt1.getAnalyzedTranslatedTokens().get(token), getTranslationComposer());
                if (tokenVector != null) {
                    t1Vectors.add(tokenVector);
                }
            }

            List<RealVector> t2Vectors = new LinkedList<>();
            MutableTranslatedTerm tt2 = p.getTranslatedT2();
            for (String token : tt2.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(tt2.getAnalyzedTranslatedTokens().get(token), getTranslationComposer());
                if (tokenVector != null) {
                    t2Vectors.add(tokenVector);
                }
            }

            vectorPair.v1 = t1Vectors.isEmpty() ? null : getTermComposer().compose(t1Vectors);
            vectorPair.v2 = t2Vectors.isEmpty() ? null : getTermComposer().compose(t2Vectors);
            res.put(p, vectorPair);
        }

        return res;
    }

    @Override
    public Map<String, RealVector> getVectors(List<AnalyzedTerm> terms) {
        if (terms == null) {
            throw new IllegalArgumentException("terms can't be null");
        }

        Set<String> allTerms = new HashSet<>();
        terms.forEach(t -> allTerms.addAll(t.getAnalyzedTokens()));

        collectVectors(allTerms, getMetadata().getDimensions());

        Map<String, RealVector> vectors = new HashMap<>();

        for (AnalyzedTerm term : terms) {
            RealVector vector = composeVectors(term.getAnalyzedTokens(), getTermComposer());
            vectors.put(term.getTerm(), vector);
        }

        return vectors;
    }

    @Override
    public Map<String, RealVector> getTranslatedVectors(List<MutableTranslatedTerm> terms) {
        if (terms == null) {
            throw new IllegalArgumentException("terms can't be null");
        }

        Set<String> allTerms = new HashSet<>();

        for (MutableTranslatedTerm mtt : terms) {
            for (String token : mtt.getAnalyzedTranslatedTokens().keySet()) {
                allTerms.addAll(mtt.getAnalyzedTranslatedTokens().get(token));
            }
        }

        collectVectors(allTerms, getMetadata().getDimensions());

        Map<String, RealVector> vectors = new HashMap<>();

        for (MutableTranslatedTerm mtt : terms) {
            List<RealVector> tokenVectors = new LinkedList<>();
            for (String token : mtt.getAnalyzedTranslatedTokens().keySet()) {
                RealVector tokenVector = composeVectors(mtt.getAnalyzedTranslatedTokens().get(token), getTranslationComposer());
                if (tokenVector != null) {
                    tokenVectors.add(tokenVector);
                }
            }

            RealVector vector = tokenVectors.isEmpty() ? null : getTermComposer().compose(tokenVectors);
            vectors.put(mtt.getTerm(), vector);
        }

        return vectors;
    }

    private RealVector composeVectors(List<String> terms, VectorComposer composer) {
        logger.trace("Composing {} vectors", terms.size());

        List<RealVector> vectors = getFromCache(composer.filter(terms));
        return composer.compose(vectors);
    }
}
