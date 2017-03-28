package org.lambda3.indra.core.tests;

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
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.core.composition.AveragedVectorComposer;
import org.lambda3.indra.core.composition.UniqueSumVectorComposer;
import org.lambda3.indra.core.impl.CosineRelatednessFunction;
import org.lambda3.indra.core.utils.ParamsUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class RelatednessDummyClient extends RelatednessClient {

    private static Random rnd = new Random();

    protected RelatednessDummyClient() {
        super(ParamsUtils.buildNoTranslateCosineDefaultParams("corpus", "EN", "ESA"),
                new MockCachedVectorSpace(new UniqueSumVectorComposer(), new AveragedVectorComposer()),
                new CosineRelatednessFunction());
    }

    @Override
    protected List<ScoredTextPair> compute(Map<? extends AnalyzedPair, VectorPair> vectorPairs) {
        return vectorPairs.keySet().stream().map(p -> new ScoredTextPair(p, rnd.nextDouble())).
                collect(Collectors.toList());
    }

    @Override
    protected List<? extends AnalyzedPair> doAnalyze(List<TextPair> pairs) {
        return pairs.stream().map(p -> new AnalyzedPair(p)).collect(Collectors.toList());
    }

    @Override
    protected Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs) {
        return analyzedPairs.stream().collect(Collectors.toMap(p -> p, p -> new VectorPair()));
    }
}
