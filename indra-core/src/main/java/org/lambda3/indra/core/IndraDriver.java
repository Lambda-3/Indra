package org.lambda3.indra.core;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.client.MutableAnalyzedTerm;
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.translation.Translator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
    private VectorSpaceFactory vectorSpaceFactory;
    private RelatednessClientFactory relatednessClientFactory;
    private Params currentParams;

    public IndraDriver(Params params, VectorSpaceFactory vectorSpaceFactory, Translator translator) {
        this.currentParams = params;
        this.vectorSpaceFactory = vectorSpaceFactory;
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory, translator);
    }

    public RelatednessResult getRelatedness(List<TextPair> pairs) {
        return getRelatedness(pairs, currentParams);
    }

    public RelatednessResult getRelatedness(List<TextPair> pairs, Params params) {
        RelatednessClient relatednessClient = relatednessClientFactory.create(params);
        RelatednessResult result = relatednessClient.getRelatedness(pairs);

        return result;
    }

    public Map<String, RealVector> getVectors(List<String> terms) {
        return this.getVectors(terms, this.currentParams);
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

    public Map<String, RealVector> getVectors(List<String> terms, Params params) {
        VectorSpace vectorSpace = vectorSpaceFactory.create(params);
        IndraAnalyzer analyzer = new IndraAnalyzer(params.language, false);

        List<MutableAnalyzedTerm> analyzedTerms = new LinkedList<>();

        for (String term : terms) {
            try {
                List<String> analyzedTokens = analyzer.analyze(term);
                //TODO broken analyzedTerms.add(new MutableAnalyzedTerm(term, analyzedTokens));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return vectorSpace.getVectors(analyzedTerms);
    }
}
