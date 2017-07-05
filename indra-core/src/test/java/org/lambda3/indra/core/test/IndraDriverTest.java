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
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.core.VectorSpace;
import org.lambda3.indra.core.VectorSpaceFactory;
import org.lambda3.indra.core.composition.VectorComposerFactory;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.lambda3.indra.client.ScoreFunction.COSINE;

public class IndraDriverTest {
    private IndraDriver driver;

    @BeforeClass
    public void beforeClass() {
        VectorSpaceFactory vectorSpaceFactory = createVectorSpaceFactor();
        IndraTranslatorFactory translatorFactory = createIndraTranslatorFactory();
        this.driver = new IndraDriver(vectorSpaceFactory, translatorFactory);
    }

    @Test
    public void nonExistingTerms() {
        final String NON_EXISTENT_TERM = "yyyyyyyywywywywy";
        List<String> terms = Arrays.asList(NON_EXISTENT_TERM, "amor");
        VectorRequest request = new VectorRequest().language("PT").terms(terms);
        Map<String, RealVector> results = driver.getVectors(request);
        Assert.assertEquals(results.size(), terms.size());
        Assert.assertNull(results.get(NON_EXISTENT_TERM));
    }

    @Test
    public void getTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe", "pai");
        VectorRequest request = new VectorRequest().language("PT").mt(true).terms(terms);
        Map<String, RealVector> res = driver.getVectors(request);
        Assert.assertEquals(res.get("mãe"), MockCachedVectorSpace.ONE_VECTOR);
        Assert.assertEquals(res.get("pai"), MockCachedVectorSpace.NEGATIVE_ONE_VECTOR);
    }

    @Test
    public void getComposedTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe computador", "pai avaliação");
        VectorRequest request = new VectorRequest().language("PT").mt(true).terms(terms);
        Map<String, RealVector> res = driver.getVectors(request);
        Assert.assertEquals(res.get(terms.get(0)), MockCachedVectorSpace.TWO_VECTOR);
        Assert.assertEquals(res.get(terms.get(1)), MockCachedVectorSpace.NEGATIVE_TWO_VECTOR);
    }

    @Test
    public void getRelatedness() {
        RelatednessPairRequest request = new RelatednessPairRequest().scoreFunction(COSINE)
                .language("PT").mt(true);
        request.pairs(Arrays.asList(new TextPair("mãe", "pai"), new TextPair("mãe computador", "pai avaliação")));
        List<ScoredTextPair> relatedness = driver.getRelatedness(request);

        for (ScoredTextPair pair : relatedness) {
            Assert.assertEquals(Math.floor(pair.score), -1d);
        }
    }

    @Test
    public void getZeroRelatedness() {
        RelatednessPairRequest request = new RelatednessPairRequest().scoreFunction(COSINE).language("PT");
        request.pairs(Arrays.asList(new TextPair("blabla", "ttt"),
                new TextPair("these tokens are not in the vector model", "neither those")));
        List<ScoredTextPair> relatedness = driver.getRelatedness(request);

        for (ScoredTextPair pair : relatedness) {
            Assert.assertEquals(pair.score, 0d);
        }
    }

    @Test
    public void oneToManyRelatednessPTMT() {
        String lang = "PT";
        boolean mt = true;
        RelatednessPairRequest pairRequest = new RelatednessPairRequest().scoreFunction(COSINE)
                .language(lang).mt(mt);
        pairRequest.pairs(Arrays.asList(new TextPair("mãe", "pai"), new TextPair("mãe", "mãe computador"),
                new TextPair("mãe", "pai avaliação")));

        RelatednessOneToManyRequest otmRequest = new RelatednessOneToManyRequest().scoreFunction(COSINE)
                .language(lang).mt(mt);
        otmRequest.one("mãe").many(Arrays.asList("pai", "mãe computador", "pai avaliação"));
        oneToManyRelatedness(pairRequest, otmRequest);
    }

    @Test
    public void oneToManyRelatednessEN() {
        String lang = "EN";
        boolean mt = false;
        RelatednessPairRequest pairRequest = new RelatednessPairRequest().scoreFunction(COSINE)
                .language(lang).mt(mt);
        pairRequest.pairs(Arrays.asList(new TextPair("throne", "plane"), new TextPair("throne", "good"),
                new TextPair("throne", "hot"), new TextPair("throne", "south"), new TextPair("throne", "hate"),
                new TextPair("throne", "car"), new TextPair("throne", "bad"), new TextPair("throne", "cold"),
                new TextPair("throne", "north"), new TextPair("throne", "hot cold bad car")));

        RelatednessOneToManyRequest otmRequest = new RelatednessOneToManyRequest().scoreFunction(COSINE)
                .language(lang).mt(mt);
        otmRequest.one("throne").many(Arrays.asList("plane", "good", "hot", "south", "hate", "car", "bad", "cold",
                "north", "hot cold bad car"));
        oneToManyRelatedness(pairRequest, otmRequest);
    }

    public void oneToManyRelatedness(RelatednessPairRequest pairRequest, RelatednessOneToManyRequest otmRequest) {
        List<ScoredTextPair> relatedness = driver.getRelatedness(pairRequest);
        Map<String, Double> pairResults = relatedness.stream().collect(Collectors.toMap(p -> p.t2, p -> p.score));

        Map<String, Double> otmRelatedness = driver.getRelatedness(otmRequest);

        for (String m : otmRelatedness.keySet()) {
            Assert.assertEquals(otmRelatedness.get(m), pairResults.get(m));
        }
    }

    public static VectorSpaceFactory createVectorSpaceFactor() {

        return new VectorSpaceFactory() {
            @Override
            protected VectorSpace doCreate(AbstractBasicRequest request) {
                VectorComposerFactory composerFactory = new VectorComposerFactory();
                return new MockCachedVectorSpace(composerFactory.getComposer(IndraDriver.DEFAULT_TERM_COMPOSTION),
                        composerFactory.getComposer(IndraDriver.DEFAULT_TRANSLATION_COMPOSTION));
            }

            @Override
            protected String createKey(AbstractBasicRequest request) {
                return request.toString();
            }

            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }
        };
    }

    public static IndraTranslatorFactory createIndraTranslatorFactory() {

        return new IndraTranslatorFactory() {
            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }

            @Override
            protected IndraTranslator doCreate(AbstractBasicRequest request) {
                return new MockIndraTranslator();
            }

            @Override
            protected String createKey(AbstractBasicRequest request) {
                return request.toString();
            }
        };
    }
}
