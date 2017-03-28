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

import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.function.RelatednessFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StandardRelatednessClient extends RelatednessClient {

    protected StandardRelatednessClient(Params params, VectorSpace vectorSpace, RelatednessFunction func) {
        super(params, vectorSpace, func);
    }

    @Override
    protected List<AnalyzedPair> doAnalyze(List<TextPair> pairs) {
        logger.debug("Analyzing {} pairs", pairs.size());

        List<AnalyzedPair> analyzedPairs = new ArrayList<>(pairs.size());
        IndraAnalyzer analyzer = new IndraAnalyzer(params.language);

        for (TextPair pair : pairs) {
            AnalyzedPair analyzedPair = analyzer.analyze(pair);
            if (analyzedPair != null) {
                analyzedPairs.add(analyzedPair);
            }
        }

        return analyzedPairs;
    }

    @Override
    protected Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs) {
        return vectorSpace.getVectorPairs((List<AnalyzedPair>) analyzedPairs);
    }

}
