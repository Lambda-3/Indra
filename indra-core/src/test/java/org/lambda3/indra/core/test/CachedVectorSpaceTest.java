package org.lambda3.indra.core.test;

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
import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.core.composition.AveragedVectorComposer;
import org.lambda3.indra.core.composition.SumVectorComposer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CachedVectorSpaceTest {

    IndraAnalyzer analyzer = new IndraAnalyzer("EN");
    MockCachedVectorSpace vectorSpace = new MockCachedVectorSpace(new SumVectorComposer(), new AveragedVectorComposer());

    @Test
    public void getSimpleVectorPairsTest() {
        AnalyzedPair analyzedPair1 = analyzer.analyze(new TextPair("love", "hate"));
        AnalyzedPair analyzedPair2 = analyzer.analyze(new TextPair("plane", "car"));
        AnalyzedPair analyzedPair3 = analyzer.analyze(new TextPair("north", "south"));
        List<AnalyzedPair> pairs = Arrays.asList(analyzedPair1, analyzedPair2, analyzedPair3);

        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectorPairs(pairs);
        Assert.assertEquals(vectorPairs.size(), 3);

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            Assert.assertTrue(pairs.contains(pair));

            VectorPair vectorPair = vectorPairs.get(pair);
            Assert.assertEquals(vectorPair.v1.add(vectorPair.v2), vectorSpace.ZERO_VECTOR);
        }
    }

    @Test
    public void getComposedVectorPairsTest() {
        AnalyzedPair analyzedPair1 = analyzer.analyze(new TextPair("love plane", "hate car"));
        AnalyzedPair analyzedPair2 = analyzer.analyze(new TextPair("good north", "bad south"));
        List<AnalyzedPair> pairs = Arrays.asList(analyzedPair1, analyzedPair2);

        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectorPairs(pairs);
        Assert.assertEquals(vectorPairs.size(), 2);

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            Assert.assertTrue(pairs.contains(pair));

            VectorPair vectorPair = vectorPairs.get(pair);
            Assert.assertEquals(vectorPair.v1.add(vectorPair.v2), vectorSpace.ZERO_VECTOR);
        }
    }

    @Test
    public void getComposedVectorTest() {
        List<String> terms = Arrays.asList("love plane good south hot", "hate car bad north cold");
        List<AnalyzedTerm> analyzedTerms = terms.stream().map(t -> new AnalyzedTerm(t, analyzer.nonStemmedAnalyze(t))).
                collect(Collectors.toList());

        Map<String, RealVector> vectorPairs = vectorSpace.getVectors(analyzedTerms);
        Assert.assertEquals(vectorPairs.size(), 2);

        Assert.assertEquals(vectorPairs.get(terms.get(0)), vectorSpace.ONE_VECTOR);
        Assert.assertEquals(vectorPairs.get(terms.get(1)), vectorSpace.NEGATIVE_ONE_VECTOR);
    }

    @Test
    public void getTranslatedPairsTest() {
        IndraAnalyzer ptAnalyzer = new IndraAnalyzer("PT");
        AnalyzedTranslatedPair analyzedPair1 = ptAnalyzer.analyzeForTranslation(new TextPair("mãe", "pai"));
        AnalyzedTranslatedPair analyzedPair2 = ptAnalyzer.analyzeForTranslation(new TextPair("computador", "avaliação"));

        analyzedPair1.getTranslatedT1().putAnalyzedTranslatedTokens("mãe", Arrays.asList("mother", "mom", "matriarch"));
        analyzedPair1.getTranslatedT2().putAnalyzedTranslatedTokens("pai", Arrays.asList("father", "dad", "patriarch"));

        analyzedPair2.getTranslatedT1().putAnalyzedTranslatedTokens("computador", Arrays.asList("machine", "computer"));
        analyzedPair2.getTranslatedT2().putAnalyzedTranslatedTokens("avaliação", Arrays.asList("test", "evaluation"));

        List<AnalyzedTranslatedPair> pairs = Arrays.asList(analyzedPair1, analyzedPair2);

        Map<AnalyzedTranslatedPair, VectorPair> vectorPairs = vectorSpace.getTranslatedVectorPairs(pairs);
        Assert.assertEquals(vectorPairs.size(), 2);

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            Assert.assertTrue(pairs.contains(pair));

            VectorPair vectorPair = vectorPairs.get(pair);
            Assert.assertEquals(vectorPair.v1, vectorSpace.ONE_VECTOR);
            Assert.assertEquals(vectorPair.v2, vectorSpace.NEGATIVE_ONE_VECTOR);
        }
    }

    @Test
    public void getComposedTranslatedPairsTest() {
        IndraAnalyzer ptAnalyzer = new IndraAnalyzer("PT");
        AnalyzedTranslatedPair analyzedPair = ptAnalyzer.analyzeForTranslation(new TextPair("mãe computador", "pai avaliação"));

        analyzedPair.getTranslatedT1().putAnalyzedTranslatedTokens("mãe", Arrays.asList("mother", "mom", "matriarch"));
        analyzedPair.getTranslatedT1().putAnalyzedTranslatedTokens("computador", Arrays.asList("machine", "computer"));

        analyzedPair.getTranslatedT2().putAnalyzedTranslatedTokens("pai", Arrays.asList("father", "dad", "patriarch"));
        analyzedPair.getTranslatedT2().putAnalyzedTranslatedTokens("avaliação", Arrays.asList("test", "evaluation"));

        List<AnalyzedTranslatedPair> pairs = Arrays.asList(analyzedPair);

        Map<AnalyzedTranslatedPair, VectorPair> vectorPairs = vectorSpace.getTranslatedVectorPairs(pairs);
        Assert.assertEquals(vectorPairs.size(), 1);

        for (AnalyzedPair pair : vectorPairs.keySet()) {
            Assert.assertTrue(pairs.contains(pair));

            VectorPair vectorPair = vectorPairs.get(pair);
            Assert.assertEquals(vectorPair.v1, vectorSpace.TWO_VECTOR);
            Assert.assertEquals(vectorPair.v2, vectorSpace.NEGATIVE_TWO_VECTOR);
        }
    }

    @Test
    public void getComposedTranslatedVectorsTest() {
        List<String> terms = Arrays.asList("mãe computador", "pai avaliação");
        List<MutableTranslatedTerm> analyzedTerms = terms.stream().map(t -> new MutableTranslatedTerm(t, analyzer.nonStemmedAnalyze(t))).
                collect(Collectors.toList());

        analyzedTerms.get(0).putAnalyzedTranslatedTokens("mãe", Arrays.asList("mother", "mom", "matriarch"));
        analyzedTerms.get(0).putAnalyzedTranslatedTokens("computador", Arrays.asList("machine", "computer"));

        analyzedTerms.get(1).putAnalyzedTranslatedTokens("pai", Arrays.asList("father", "dad", "patriarch"));
        analyzedTerms.get(1).putAnalyzedTranslatedTokens("avaliação", Arrays.asList("test", "evaluation"));

        Map<String, RealVector> vectorPairs = vectorSpace.getTranslatedVectors(analyzedTerms);
        Assert.assertEquals(vectorPairs.size(), 2);

        Assert.assertEquals(vectorPairs.get(terms.get(0)), vectorSpace.TWO_VECTOR);
        Assert.assertEquals(vectorPairs.get(terms.get(1)), vectorSpace.NEGATIVE_TWO_VECTOR);
    }

}
