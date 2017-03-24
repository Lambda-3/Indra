package org.lambda3.indra.driver;

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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.*;
import org.lambda3.indra.core.impl.MongoVectorSpaceFactory;

import java.io.IOException;
import java.util.*;

public class IndraDriver {

    private static final String DEFAULT_CORPUS_NAME = "wiki-2014";
    private static final String DEFAULT_LANGUAGE = "EN";
    private static final String DEFAULT_DISTRIBUTIONAL_MODEL = "W2V";
    private static final Params DEFAULT_PARAMS = new Params(DEFAULT_CORPUS_NAME, ScoreFunction.COSINE, DEFAULT_LANGUAGE, DEFAULT_DISTRIBUTIONAL_MODEL);

    private static final String DEFAULT_MONGO_URI = null;

    private MongoVectorSpaceFactory vectorSpaceFactory;
    private RelatednessClientFactory relatednessClientFactory;
    private Params currentParams;

    public IndraDriver() {
        this(DEFAULT_PARAMS);
    }

    public IndraDriver(Params params) {
        this(params, new MongoVectorSpaceFactory(DEFAULT_MONGO_URI));
    }

    public IndraDriver(Params params, MongoVectorSpaceFactory vectorSpaceFactory) {
        this.currentParams = params;
        this.vectorSpaceFactory = vectorSpaceFactory;
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory);
    }

    public Collection<ScoredTextPair> getRelatedness(List<TextPair> pairs) {
        return getRelatedness(pairs, currentParams);
    }
    
    public Collection<ScoredTextPair> getRelatedness(List<TextPair> pairs, Params params) {
        RelatednessClient relatednessClient = relatednessClientFactory.create(params);
        RelatednessResult result = relatednessClient.getRelatedness(pairs);

        return result.getScores();
    }

    public Map<String, RealVector> getVectors(List<String> terms) {
        return this.getVectors(terms, this.currentParams);
    }

    public Map<String, RealVector> getVectors(List<String> terms, Params params) {
        VectorSpace vectorSpace = vectorSpaceFactory.create(params);
        IndraAnalyzer analyzer = new IndraAnalyzer(params.language, params.useStemming());

        List<AnalyzedTerm> analyzedTerms = new LinkedList<>();

        for (String term : terms) {
            try {
                List<String> analyzedTokens = analyzer.analyze(term);
                analyzedTerms.add(new AnalyzedTerm(term, analyzedTokens));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<AnalyzedTerm, Map<Integer, Double>> inVectors = vectorSpace.getVectors(analyzedTerms);
        Map<String, RealVector> outVectors = new HashMap();

        for (AnalyzedTerm term : inVectors.keySet()) {
            double[] dv = inVectors.get(term).values().stream().mapToDouble(d -> d).toArray();
            if (vectorSpace.isSparse()) {
                outVectors.put(term.getTerm(), new OpenMapRealVector(dv));
            } else {
                outVectors.put(term.getTerm(), new ArrayRealVector(dv));
            }
        }

        return outVectors;
    }
}
