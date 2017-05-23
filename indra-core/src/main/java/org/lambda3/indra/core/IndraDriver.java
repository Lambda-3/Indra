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
import org.lambda3.indra.core.composition.VectorComposition;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class IndraDriver {
    public static final VectorComposition DEFAULT_TERM_COMPOSTION = VectorComposition.UNIQUE_SUM;
    public static final VectorComposition DEFAULT_TRANSLATION_COMPOSTION = VectorComposition.AVERAGE;

    private Logger logger = LoggerFactory.getLogger(getClass());

    private VectorSpaceFactory vectorSpaceFactory;
    private IndraTranslatorFactory translatorFactory;
    private RelatednessClientFactory relatednessClientFactory;

    public IndraDriver(VectorSpaceFactory vectorSpaceFactory, IndraTranslatorFactory translatorFactory) {
        this.vectorSpaceFactory = Objects.requireNonNull(vectorSpaceFactory);
        this.translatorFactory = Objects.requireNonNull(translatorFactory);
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory, translatorFactory);
    }

    //TODO pair is into request
    public final RelatednessResult getRelatedness(RelatednessPairRequest request) {
        logger.trace("getting relatedness for {} pairs (request={})", request.getPairs(), request);
        RelatednessClient relatednessClient = relatednessClientFactory.create(request);
        RelatednessResult result = relatednessClient.getRelatedness(request.getPairs());
        logger.trace("done");
        return result;
    }

    public final RelatednessResult getRelatedness(RelatednessOneToManyRequest request) {
        //TODO implement me.
        return null;
    }

    public final Map<String, RealVector> getVectors(List<String> terms, VectorRequest request) {
        logger.trace("getting vectors for {} terms (params={})", terms.size(), request);
        VectorSpace vectorSpace = vectorSpaceFactory.create(request);
        ModelMetadata modelMetadata = vectorSpace.getMetadata();

        if (request.isMt()) {
            ModelMetadata translationModelMetadata = vectorSpace.getMetadata();
            IndraAnalyzer nativeLangAnalyzer = new IndraAnalyzer(request.getLanguage(), translationModelMetadata);

            logger.trace("applying translation");
            List<MutableTranslatedTerm> translatedTerms = new LinkedList<>();
            for (String term : terms) {
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
            logger.trace("done");
            return vectorSpace.getTranslatedVectors(translatedTerms);

        } else {
            IndraAnalyzer basicAnalyzer = new IndraAnalyzer(request.getLanguage(), modelMetadata);
            List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
            for (String term : terms) {
                analyzedTerms.add(new AnalyzedTerm(term, basicAnalyzer.analyze(term)));
            }
            logger.trace("done");
            return vectorSpace.getVectors(analyzedTerms);
        }
    }

    public final Map<String, double[]> getVectorsAsArray(List<String> terms, VectorRequest request) {
        Map<String, RealVector> inVectors = getVectors(terms, request);

        Map<String, double[]> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? realVector.toArray() : null);
        }

        return outVectors;
    }

    public final Map<String, Map<Integer, Double>> getVectorsAsMap(List<String> terms, VectorRequest request) {
        Map<String, RealVector> inVectors = getVectors(terms, request);

        Map<String, Map<Integer, Double>> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);

            outVectors.put(term, realVector != null ? RealVectorUtil.vectorToMap(realVector) : null);
        }

        return outVectors;
    }


}
