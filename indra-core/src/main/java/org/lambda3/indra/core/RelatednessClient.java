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

import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.ScoredTextPair;
import org.lambda3.indra.common.client.TextPair;
import org.lambda3.indra.core.exception.RelatednessError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RelatednessClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract List<ScoredTextPair> compute(List<AnalyzedPair> pairs);

    protected abstract Params getParams();

    private AnalyzedPair doAnalyze(IndraAnalyzer analyzer, TextPair p) {
        try {
            return analyzer.analyze(p);
        } catch (IOException e) {
            logger.error("Error analyzing {}", p, e);
        }
        return null;
    }

    private List<ScoredTextPair> doCompute(List<TextPair> pairs)  {
        logger.debug("Analyzing {} pairs", pairs.size());
        try {
            IndraAnalyzer analyzer = new IndraAnalyzer(getParams().language, getParams().useStemming());
            List<AnalyzedPair> analyzedPairs = new ArrayList<>();
            pairs.forEach(p -> {
                AnalyzedPair analyzedPair = doAnalyze(analyzer, p);
                if (analyzedPair != null)
                    analyzedPairs.add(doAnalyze(analyzer, p));
            });

            logger.debug("Computing relatedness..");
            List<ScoredTextPair> r = compute(analyzedPairs);
            logger.debug("Done.");
            return r;
        }
        catch(Exception e) {
            throw new RelatednessError(e);
        }
    }

    public final RelatednessResult getRelatedness(List<TextPair> pairs) {
        return new RelatednessResult(doCompute(pairs));
    }
}
