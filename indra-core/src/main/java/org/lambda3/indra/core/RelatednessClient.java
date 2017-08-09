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
import org.lambda3.indra.core.composition.VectorComposer;
import org.lambda3.indra.core.filter.Filter;
import org.lambda3.indra.core.function.RelatednessFunction;
import org.lambda3.indra.Threshold;
import org.lambda3.indra.core.utils.MapUtils;
import org.lambda3.indra.core.vs.VectorSpace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public abstract class RelatednessClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected VectorSpace vectorSpace;
    protected RelatednessRequest request;
    protected RelatednessFunction func;
    protected VectorComposer termComposer;
    protected VectorComposer translationComposer;

    protected RelatednessClient(RelatednessRequest request, VectorSpace vectorSpace, RelatednessFunction func,
                                VectorComposer termComposer, VectorComposer translationComposer) {
        this.vectorSpace = Objects.requireNonNull(vectorSpace);
        this.request = Objects.requireNonNull(request);
        this.func = Objects.requireNonNull(func);
        this.termComposer = Objects.requireNonNull(termComposer);
        this.translationComposer = translationComposer;
    }

    protected abstract List<AnalyzedPair> doAnalyzePairs(List<TextPair> pairs);

    protected abstract List<AnalyzedTerm> doAnalyze(String one, List<String> terms);

    protected abstract Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs);

    protected List<ScoredTextPair> compute(Map<? extends AnalyzedPair, VectorPair> vectorPairs) {
        Queue<ScoredTextPair> scoredTextPairs = new ConcurrentLinkedQueue<>();

        vectorPairs.entrySet().stream().parallel().forEach(entry -> {
            AnalyzedPair pair = entry.getKey();
            VectorPair vectorPair = entry.getValue();

            if (vectorPair.v1 != null && vectorPair.v2 != null) {
                scoredTextPairs.add(new ScoredTextPair(pair,
                        func.sim(vectorPair.v1, vectorPair.v2, vectorSpace.getMetadata().isSparse())));
            } else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }
        });

        //TODO REVIEW HERE
        return new LinkedList<>(scoredTextPairs);
    }

    public final List<ScoredTextPair> getRelatedness(List<TextPair> pairs) {
        List<AnalyzedPair> analyzedPairs = doAnalyzePairs(pairs);
        Map<? extends AnalyzedPair, VectorPair> vectorsPairs = getVectors(analyzedPairs);
        return compute(vectorsPairs);
    }

    LinkedHashMap<String, Double> getRelatedness(String one, List<String> many, Threshold threshold, boolean translated) {
        List<? extends AnalyzedTerm> analyzedTerms = doAnalyze(one, many);
        return getRelatedness(one, many, analyzedTerms, threshold, translated);
    }

    private LinkedHashMap<String, Double> getRelatedness(String one, Collection<String> many, List<? extends AnalyzedTerm> analyzedTerms,
                                                         Threshold threshold, boolean translated) {
        Map<String, RealVector> vectors;
        if (translated) {
            vectors = vectorSpace.getTranslatedVectors((List<MutableTranslatedTerm>) analyzedTerms,
                    termComposer, translationComposer);
        } else {
            vectors = vectorSpace.getVectors((List<AnalyzedTerm>) analyzedTerms, termComposer);
        }
        Map<String, Double> results = new ConcurrentHashMap<>();

        RealVector oneVector = vectors.get(one);

        many.stream().parallel().forEach(m -> {
            RealVector mVector = vectors.get(m);
            if (oneVector != null && mVector != null) {
                double score = func.sim(oneVector, mVector, vectorSpace.getMetadata().isSparse());
                results.put(m, score);
            } else {
                results.put(m, 0d);
            }
        });

        LinkedHashMap<String, Double> sortedResults = MapUtils.entriesSortedByValues(results);
        if (threshold != null) {
            threshold.apply(sortedResults);
        }
        return sortedResults;
    }

    Map<String, Map<String, Double>> getNeighborRelatedness(List<String> terms, int topk, Threshold threshold, Filter binaryFilter) {
        logger.trace("getting neighbors Relatedness for {} terms and {} topk", terms.size(), topk);
        List<AnalyzedTerm> analyzedTerms = doAnalyze(null, terms);

        Map<String, Map<String, Double>> results = new HashMap<>();
        analyzedTerms.stream().parallel().forEach(at -> {
            Collection<String> nearestTerms = vectorSpace.getNearestTerms(at, topk);

            List<AnalyzedTerm> analyzedNeighbors = nearestTerms.stream().map(
                    t -> new AnalyzedTerm(t, Collections.singletonList(t))).collect(Collectors.toList());

            Map<String, Double> relatedness = getRelatedness(at.getFirstToken(), nearestTerms,
                    analyzedNeighbors, threshold, false);

            results.put(at.getTerm(), relatedness);
        });

        logger.trace("done");
        return results;
    }
}
