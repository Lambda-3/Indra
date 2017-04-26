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
import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.MutableTranslatedTerm;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public abstract class IndraDriver {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private VectorSpaceFactory<? extends VectorSpace> vectorSpaceFactory;
    private IndraTranslatorFactory<? extends IndraTranslator> translatorFactory;
    private RelatednessClientFactory relatednessClientFactory;
    private Params currentParams;

    public IndraDriver(Params params, VectorSpaceFactory<? extends VectorSpace> vectorSpaceFactory,
                       IndraTranslatorFactory<? extends IndraTranslator> translatorFactory) {
        this.currentParams = Objects.requireNonNull(params);
        this.vectorSpaceFactory = Objects.requireNonNull(vectorSpaceFactory);
        this.translatorFactory = Objects.requireNonNull(translatorFactory);
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory, translatorFactory);
    }

    public final RelatednessResult getRelatedness(List<TextPair> pairs) {
        return getRelatedness(pairs, currentParams);
    }

    public final RelatednessResult getRelatedness(List<TextPair> pairs, Params params) {
        logger.trace("getting relatedness for {} pairs (params={})", pairs.size(), params);
        RelatednessClient relatednessClient = relatednessClientFactory.create(params);
        RelatednessResult result = relatednessClient.getRelatedness(pairs);
        logger.trace("done");
        return result;
    }

    public final Map<String, RealVector> getVectors(List<String> terms) {
        return this.getVectors(terms, this.currentParams);
    }

    public final Map<String, RealVector> getVectors(List<String> terms, Params params) {
        logger.trace("getting vectors for {} terms (params={})", terms.size(), params);
        VectorSpace vectorSpace = vectorSpaceFactory.create(params);
        ModelMetadata modelMetadata = vectorSpace.getMetadata();

        if (params.translate) {
            ModelMetadata translationModelMetadata = vectorSpace.getMetadata();
            IndraAnalyzer<AnalyzedPair> nativeLangAnalyzer = new IndraAnalyzer<>(params.language, translationModelMetadata, AnalyzedPair.class);

            logger.trace("applying translation");
            List<MutableTranslatedTerm> translatedTerms = new LinkedList<>();
            for (String term : terms) {
                List<String> analyzedTokens = nativeLangAnalyzer.analyze(term);
                translatedTerms.add(new MutableTranslatedTerm(term, analyzedTokens));
            }

            IndraTranslator translator = translatorFactory.create(params);
            translator.translate(translatedTerms);

            for (MutableTranslatedTerm term : translatedTerms) {
                for (String token : term.getTranslatedTokens().keySet()) {
                    List<String> translatedTokens = term.getTranslatedTokens().get(token);
                    if (modelMetadata.isApplyStemmer()) {
                        translatedTokens = IndraAnalyzer.stem(translatedTokens, params.translateTargetLanguage);
                    }

                    term.putAnalyzedTranslatedTokens(token, translatedTokens);
                }
            }
            logger.trace("done");
            return vectorSpace.getTranslatedVectors(translatedTerms);

        } else {
            IndraAnalyzer basicAnalyzer = new IndraAnalyzer(params.language, modelMetadata, AnalyzedPair.class);
            List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
            for (String term : terms) {
                analyzedTerms.add(new AnalyzedTerm(term, basicAnalyzer.analyze(term)));
            }
            logger.trace("done");
            return vectorSpace.getVectors(analyzedTerms);
        }
    }

    public final Map<String, double[]> getVectorsAsArray(List<String> terms, Params params) {
        Map<String, RealVector> inVectors = getVectors(terms, params);

        Map<String, double[]> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? realVector.toArray() : null);
        }

        return outVectors;
    }

    public final Map<String, Map<Integer, Double>> getVectorsAsMap(List<String> terms, Params params) {
        Map<String, RealVector> inVectors = getVectors(terms, params);

        Map<String, Map<Integer, Double>> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? RealVectorUtil.vectorToMap(realVector) : null);
        }

        return outVectors;
    }


}
