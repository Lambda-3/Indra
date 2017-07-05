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
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.client.*;
import org.lambda3.indra.core.composition.VectorComposer;
import org.lambda3.indra.core.composition.VectorComposerFactory;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class IndraDriver {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private VectorSpaceFactory vectorSpaceFactory;
    private IndraTranslatorFactory translatorFactory;
    private RelatednessClientFactory relatednessClientFactory;
    private VectorComposerFactory vectorComposerFactory = new VectorComposerFactory();

    public IndraDriver(VectorSpaceFactory vectorSpaceFactory, IndraTranslatorFactory translatorFactory) {
        this.vectorSpaceFactory = Objects.requireNonNull(vectorSpaceFactory);
        this.translatorFactory = Objects.requireNonNull(translatorFactory);
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory, translatorFactory);
    }

    public final List<ScoredTextPair> getRelatedness(RelatednessPairRequest request) {
        logger.trace("getting relatedness for {} pairs (request={})", request.getPairs().size(), request);

        RelatednessClient relatednessClient = relatednessClientFactory.create(request);
        List<ScoredTextPair> scoredPairs = relatednessClient.getRelatedness(request.getPairs());
        logger.trace("done");
        return scoredPairs;
    }

    public final Map<String, Double> getRelatedness(RelatednessOneToManyRequest request) {
        logger.trace("getting relatedness for one {} to many (size){} (request={})", request.getOne(),
                request.getMany().size(), request);

        RelatednessClient relatednessClient = relatednessClientFactory.create(request);
        Map<String, Double> scores = relatednessClient.getRelatedness(request.getOne(), request.getMany(),
                request.isMt());
        logger.trace("done");
        return scores;
    }

    public final boolean isSparseModel(VectorRequest request) {
        return vectorSpaceFactory.create(request).getMetadata().isSparse();
    }

    public final Map<String, RealVector> getVectors(VectorRequest request) {
        logger.trace("getting vectors for {} terms (request={})", request.getTerms().size(), request);
        VectorSpace vectorSpace = vectorSpaceFactory.create(request);
        ModelMetadata modelMetadata = vectorSpace.getMetadata();
        VectorComposer termComposer = vectorComposerFactory.getComposer(
                VectorComposition.valueOf(request.getTermComposition()));

        if (request.isMt()) {
            ModelMetadata translationModelMetadata = vectorSpace.getMetadata();
            IndraAnalyzer nativeLangAnalyzer = new IndraAnalyzer(request.getLanguage(), translationModelMetadata);

            logger.trace("applying translation");
            List<MutableTranslatedTerm> translatedTerms = new LinkedList<>();
            for (String term : request.getTerms()) {
                List<String> analyzedTokens = nativeLangAnalyzer.analyze(term);
                translatedTerms.add(new MutableTranslatedTerm(term, analyzedTokens));
            }

            IndraTranslator translator = translatorFactory.create(request);
            translator.translate(translatedTerms);

            for (MutableTranslatedTerm term : translatedTerms) {
                for (String token : term.getTranslatedTokens().keySet()) {
                    List<String> translatedTokens = term.getTranslatedTokens().get(token);
                    if (modelMetadata.getApplyStemmer() > 0) {
                        translatedTokens = IndraAnalyzer.stem(translatedTokens, IndraTranslator.DEFAULT_TRANSLATION_TARGET_LANGUAGE,
                                modelMetadata.getApplyStemmer());
                    }

                    term.putAnalyzedTranslatedTokens(token, translatedTokens);
                }
            }

            VectorComposer translationComposer = vectorComposerFactory.getComposer(
                    VectorComposition.valueOf(request.getTranslationComposition()));

            logger.trace("done");
            return vectorSpace.getTranslatedVectors(translatedTerms, termComposer, translationComposer);

        } else {
            IndraAnalyzer basicAnalyzer = new IndraAnalyzer(request.getLanguage(), modelMetadata);
            List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
            for (String term : request.getTerms()) {
                analyzedTerms.add(new AnalyzedTerm(term, basicAnalyzer.analyze(term)));
            }
            logger.trace("done");
            return vectorSpace.getVectors(analyzedTerms, termComposer);
        }
    }

    public final Map<String, double[]> getVectorsAsArray(VectorRequest request) {
        Map<String, RealVector> inVectors = getVectors(request);

        Map<String, double[]> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? realVector.toArray() : null);
        }

        return outVectors;
    }

    public final Map<String, Map<Integer, Double>> getVectorsAsMap(VectorRequest request) {
        Map<String, RealVector> inVectors = getVectors(request);

        Map<String, Map<Integer, Double>> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? RealVectorUtil.vectorToMap(realVector) : null);
        }

        return outVectors;
    }

    public final Map<String, Map<String, float[]>> getNeighborsVectors(NeighborsVectorsRequest request) {
        logger.trace("getting neighbors vectors for {} terms (request={})", request.getTerms().size(), request);
        VectorSpace vectorSpace = vectorSpaceFactory.create(request);

        IndraAnalyzer analyzer = new IndraAnalyzer(request.getLanguage(), vectorSpace.getMetadata());
        List<AnalyzedTerm> analyzedTerms = new LinkedList<>();

        for (String term : request.getTerms()) {
            analyzedTerms.add(new AnalyzedTerm(term, analyzer.analyze(term)));
        }

        Map<String, Map<String, float[]>> results = new ConcurrentHashMap<>();
        analyzedTerms.stream().parallel().forEach(at -> {
            Map<String, float[]> vectors = vectorSpace.getNearestVectors(at, request.getTopk());
            results.put(at.getTerm(), vectors);
        });

        logger.trace("done");
        return results;
    }

    public final Map<String, Map<String, Double>> getNeighborRelatedness(NeighborRelatednessRequest request) {
        logger.trace("getting neighbors relatedness for {} terms (request={})", request.getTerms().size(), request);
        RelatednessClient relatednessClient = relatednessClientFactory.create(request);
        Map<String, Map<String, Double>> relatedness = relatednessClient.getNeighborRelatedness(request.getTerms(),
                request.getTopk());

        logger.trace("done");
        return relatedness;
    }
}
