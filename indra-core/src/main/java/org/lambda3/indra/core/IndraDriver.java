package org.lambda3.indra.core;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.MutableTranslatedTerm;
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
public abstract class IndraDriver {

    private static final String DEFAULT_CORPUS_NAME = "wiki-2014";
    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String DEFAULT_DISTRIBUTIONAL_MODEL = "W2V";

    protected static final Params DEFAULT_PARAMS = new Params(DEFAULT_CORPUS_NAME, ScoreFunction.COSINE,
            DEFAULT_LANGUAGE, DEFAULT_DISTRIBUTIONAL_MODEL);

    private Logger logger = LoggerFactory.getLogger(getClass());

    private VectorSpaceFactory<VectorSpace> vectorSpaceFactory;
    private IndraTranslatorFactory<IndraTranslator> translatorFactory;
    private RelatednessClientFactory relatednessClientFactory;
    private Params currentParams;
    private Map<Params, RelatednessClient> clientCache = new HashMap<>();

    public IndraDriver(Params params, VectorSpaceFactory vectorSpaceFactory, IndraTranslatorFactory translatorFactory) {
        this.currentParams = params;
        this.vectorSpaceFactory = vectorSpaceFactory;
        this.translatorFactory = translatorFactory;
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory, translatorFactory);
    }

    public RelatednessClient getClient(Params params) {
        RelatednessClient relatednessClient = this.clientCache.get(params);

        if (relatednessClient == null) {
            relatednessClient = relatednessClientFactory.create(params);
            this.clientCache.put(params, relatednessClient);
        }

        return relatednessClient;
    }

    public RelatednessResult getRelatedness(List<TextPair> pairs) {
        return getRelatedness(pairs, currentParams);
    }

    public RelatednessResult getRelatedness(List<TextPair> pairs, Params params) {
        RelatednessClient relatednessClient = getClient(params);
        RelatednessResult result = relatednessClient.getRelatedness(pairs);

        return result;
    }

    public Map<String, RealVector> getVectors(List<String> terms) {
        return this.getVectors(terms, this.currentParams);
    }

    public Map<String, RealVector> getVectors(List<String> terms, Params params) {
        VectorSpace vectorSpace = vectorSpaceFactory.create(params);
        IndraAnalyzer analyzer = new IndraAnalyzer(params.language);

        if (params.translate) {
            List<MutableTranslatedTerm> translatedTerms = new LinkedList<>();
            for (String term : terms) {
                List<String> analyzedTokens = analyzer.nonStemmedAnalyze(term);
                translatedTerms.add(new MutableTranslatedTerm(term, analyzedTokens));
            }

            IndraTranslator translator = translatorFactory.create(params);
            translator.translate(translatedTerms);

            IndraAnalyzer targetAnalyzer = new IndraAnalyzer(translator.getTargetLanguage());
            for (MutableTranslatedTerm term : translatedTerms) {
                for (String token : term.getTranslatedTokens().keySet()) {
                    term.putAnalyzedTranslatedTokens(token, targetAnalyzer.stem(term.getTranslatedTokens().get(token)));
                }
            }

            return vectorSpace.getTranslatedVectors(translatedTerms);

        } else {

            List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
            for (String term : terms) {
                analyzedTerms.add(new AnalyzedTerm(term, analyzer.stemmedAnalyze(term)));
            }

            return vectorSpace.getVectors(analyzedTerms);
        }
    }

    public Map<String, Map<Integer, Double>> getVectorsAsMap(List<String> terms, Params params) {
        Map<String, RealVector> inVectors = getVectors(terms, params);

        Map<String, Map<Integer, Double>> outVectors = new HashMap<>();
        for (String term : inVectors.keySet()) {
            RealVector realVector = inVectors.get(term);
            outVectors.put(term, RealVectorUtil.vectorToMap(realVector));
        }

        return outVectors;
    }


}
