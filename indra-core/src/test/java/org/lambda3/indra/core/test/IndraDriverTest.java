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
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.*;
import org.lambda3.indra.core.composition.VectorComposerFactory;
import org.lambda3.indra.core.composition.VectorComposition;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class IndraDriverTest {

    private Params params;
    private IndraDriver driver;

    @BeforeClass
    public void beforeClass() {
        params = new Params("", ScoreFunction.COSINE, "PT", "", true, null, null,
                VectorComposition.SUM, VectorComposition.AVERAGE);

        VectorSpaceFactory vectorSpaceFactory = createVectorSpaceFactor();
        IndraTranslatorFactory translatorFactory = createIndraTranslatorFactory();
        this.driver = new IndraDriver(vectorSpaceFactory, translatorFactory);
    }

    @Test
    public void nonExistingTerms() {
        final String NON_EXISTENT_TERM = "yyyyyyyywywywywy";
        List<String> terms = Arrays.asList(NON_EXISTENT_TERM, "amor");
        Map<String, RealVector> results = driver.getVectors(terms, params);
        Assert.assertEquals(results.size(), terms.size());
        Assert.assertNull(results.get(NON_EXISTENT_TERM));
    }

    @Test
    public void getTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe", "pai");
        Map<String, RealVector> res = driver.getVectors(terms, params);
        Assert.assertEquals(res.get("mãe"), MockCachedVectorSpace.ONE_VECTOR);
        Assert.assertEquals(res.get("pai"), MockCachedVectorSpace.NEGATIVE_ONE_VECTOR);
    }

    @Test
    public void getComposedTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe computador", "pai avaliação");
        Map<String, RealVector> res = driver.getVectors(terms, params);
        Assert.assertEquals(res.get(terms.get(0)), MockCachedVectorSpace.TWO_VECTOR);
        Assert.assertEquals(res.get(terms.get(1)), MockCachedVectorSpace.NEGATIVE_TWO_VECTOR);
    }

    @Test
    public void getRelatedness() {
        RelatednessResult res = driver.getRelatedness(Arrays.asList(new TextPair("mãe", "pai"),
                new TextPair("mãe computador", "pai avaliação")), params);

        for (ScoredTextPair pair : res.getScores()) {
            Assert.assertEquals(Math.floor(pair.score), -1d);
        }
    }

    @Test
    public void getZeroRelatedness() {
        RelatednessResult res = driver.getRelatedness(Arrays.asList(new TextPair("blabla", "ttt"),
                new TextPair("these tokens are not in the vector model", "neither those")), params);

        for (ScoredTextPair pair : res.getScores()) {
            Assert.assertEquals(pair.score, 0d);
        }
    }

    private static VectorSpaceFactory createVectorSpaceFactor() {
        VectorSpaceFactory factory = new VectorSpaceFactory() {
            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }

            @Override
            protected VectorSpace doCreate(Params params) {
                VectorComposerFactory composerFactory = new VectorComposerFactory();
                return new MockCachedVectorSpace(composerFactory.getComposer(params.termComposition),
                        composerFactory.getComposer(params.translationComposition));
            }

            @Override
            protected Object createKey(Params params) {
                return params;
            }
        };

        return factory;
    }

    private static IndraTranslatorFactory createIndraTranslatorFactory() {
        IndraTranslatorFactory factory = new IndraTranslatorFactory() {
            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }

            @Override
            protected IndraTranslator doCreate(Params params) {
                return new MockIndraTranslator();
            }

            @Override
            protected Object createKey(Params params) {
                return params;
            }
        };

        return factory;
    }
}
