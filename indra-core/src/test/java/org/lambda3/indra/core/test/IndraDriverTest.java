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
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.MetadataIO;
import org.lambda3.indra.ScoredTextPair;
import org.lambda3.indra.TextPair;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.core.annoy.AnnoyVectorSpaceFactory;
import org.lambda3.indra.core.lucene.LuceneTranslatorFactory;
import org.lambda3.indra.core.lucene.LuceneVectorSpaceFactory;
import org.lambda3.indra.core.translation.TranslatorFactory;
import org.lambda3.indra.core.vs.HubVectorSpaceFactory;
import org.lambda3.indra.core.vs.VectorSpaceFactory;
import org.lambda3.indra.exception.ModelNotFoundException;
import org.lambda3.indra.model.ModelMetadata;
import org.lambda3.indra.pp.StandardPreProcessorIterator;
import org.lambda3.indra.request.RelatednessOneToManyRequest;
import org.lambda3.indra.request.RelatednessPairRequest;
import org.lambda3.indra.request.VectorRequest;
import org.lambda3.indra.util.*;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndraDriverTest {
    private static final String MODELS = "models";
    private static final String TRANSLATIONS = "translations";

    private static final String LANG = "en";

    private IndraDriver driver;

    @BeforeTest
    private void createFactory() {
        driver = new IndraDriver(createVectorSpaceFactory(), createTranslatorFactory());
    }


    public static VectorSpaceFactory createVectorSpaceFactory() {
        String modelBaseDir = IndraDriverTest.class.getClassLoader().getResource(MODELS).getPath();

        VectorSpaceFactory annoyFactory = new AnnoyVectorSpaceFactory(Paths.get(modelBaseDir, "annoy").toFile());
        VectorSpaceFactory luceneFactory = new LuceneVectorSpaceFactory(Paths.get(modelBaseDir, "lucene").toFile());

        HubVectorSpaceFactory factory = new HubVectorSpaceFactory();
        factory.addFactory(annoyFactory);
        factory.addFactory(luceneFactory);

        return factory;
    }

    public static TranslatorFactory createTranslatorFactory() {
        String translationBaseDir = IndraDriverTest.class.getClassLoader().getResource(TRANSLATIONS).getPath();
        return new LuceneTranslatorFactory(new File(translationBaseDir));
    }

    private VectorRequest createVectorRequest(String model, String corpus, String term) {
        return new VectorRequest().terms(Collections.singletonList(term)).corpus(corpus).language(LANG).model(model);
    }

    private <V extends Vector> RawSpaceModel<V> getRawSpaceModel(String model, String corpus, Class<V> clazz) {
        String resourcesDir = getClass().getClassLoader().getResource(String.format(MODELS, model)).getPath();
        String modelDir = Paths.get(resourcesDir, "raw", String.format("%s-%s-%s", model, LANG, corpus)).toString();
        
        return new RawSpaceModel<>(modelDir, clazz);
    }

    private <V extends Vector> void simpleVectorTest(String model, Class<V> clazz) throws FileNotFoundException {
        final String CORPUS = "frei";
        RawSpaceModel<V> rsm = getRawSpaceModel(model, CORPUS, clazz);
        VectorIterator<V> iter = rsm.getVectorIterator();

        while (iter.hasNext()) {
            V v = iter.next();
            if (!v.term.equalsIgnoreCase(StandardPreProcessorIterator.NUMBER_PLACEHOLDER)) {
                Assert.assertNotNull(v.content);

                Map<String, RealVector> results = driver.getVectors(createVectorRequest(model, CORPUS, v.term));
                RealVector rv = results.get(v.term);

                RealVector rvo = rsm.modelMetadata.sparse ? v.content : RealVectorUtil.loosePrecision(v.content);
                Assert.assertEquals(rvo, rv);
            }
        }
    }

    @Test
    public void equivalent() {
        final String CORPUS = "simple";
        final String DENSE = "dense";
        final String SPARSE = "sparse";

        RawSpaceModel<DenseVector> rsm = getRawSpaceModel(DENSE, CORPUS, DenseVector.class);

        try {
            VectorIterator<DenseVector> iter = rsm.getVectorIterator();

            while (iter.hasNext()) {
                DenseVector v = iter.next();
                if (!v.term.equalsIgnoreCase(StandardPreProcessorIterator.NUMBER_PLACEHOLDER)) {
                    Assert.assertNotNull(v.content);
                    RealVector rvo = RealVectorUtil.loosePrecision(v.content);

                    Map<String, RealVector> results = driver.getVectors(createVectorRequest(DENSE, CORPUS, v.term));
                    RealVector rvDense = results.get(v.term);

                    results = driver.getVectors(createVectorRequest(SPARSE, CORPUS, v.term));
                    RealVector rvSparse = results.get(v.term);

                    Assert.assertEquals(rvDense.toArray(), rvSparse.toArray());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void gloveAnnoyTest() {
        try {
            simpleVectorTest("glove", DenseVector.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void esaLuceneTest() {
        try {
            simpleVectorTest("esa", SparseVector.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void lsaAnnoyTest() {
        try {
            simpleVectorTest("lsa", DenseVector.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void w2vAnnoyTest() {
        try {
            simpleVectorTest("w2v", DenseVector.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(expectedExceptions = ModelNotFoundException.class)
    public void nonExistingModel() {
        final String NON_EXISTENT_TERM = "yyyyyyyywywywywy";
        List<String> terms = Arrays.asList(NON_EXISTENT_TERM, "amor");
        VectorRequest request = new VectorRequest().language("PT").terms(terms).termComposition("SUM").model("dense").
                corpus("simple").translationComposition("AVERAGE");
        Map<String, RealVector> results = driver.getVectors(request);
    }

    @Test
    public void nonExistingTerms() {
        final String NON_EXISTENT_TERM = "yyyyyyyywywywywy";
        List<String> terms = Arrays.asList(NON_EXISTENT_TERM, "amor");
        VectorRequest request = new VectorRequest().language("PT").terms(terms).termComposition("SUM").model("dense").
                mt(true).corpus("simple").translationComposition("AVERAGE");
        Map<String, RealVector> results = driver.getVectors(request);
        Assert.assertEquals(results.size(), terms.size());
        Assert.assertNull(results.get(NON_EXISTENT_TERM));
    }

    @Test
    public void getTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe", "pai");
        VectorRequest request = new VectorRequest().language("PT").mt(true).terms(terms).termComposition("SUM").
                translationComposition("AVERAGE").model("dense").corpus("simple");
        Map<String, RealVector> res = driver.getVectors(request);
        Assert.assertEquals(res.get("mãe"), AbstractVectorSpaceTest.ONE_VECTOR);
        Assert.assertEquals(res.get("pai"), AbstractVectorSpaceTest.NEGATIVE_ONE_VECTOR);
    }

    @Test
    public void getComposedTranslatedVectors() {
        List<String> terms = Arrays.asList("mãe computador", "pai avaliação");
        VectorRequest request = new VectorRequest().model("dense").corpus("simple").language("PT").mt(true).
                terms(terms).termComposition("SUM").translationComposition("AVERAGE");

        Map<String, RealVector> res = driver.getVectors(request);
        Assert.assertEquals(res.get(terms.get(0)), AbstractVectorSpaceTest.TWO_VECTOR);
        Assert.assertEquals(res.get(terms.get(1)), AbstractVectorSpaceTest.NEGATIVE_TWO_VECTOR);
    }

    @Test
    public void getRelatedness() {
        RelatednessPairRequest request = new RelatednessPairRequest().scoreFunction("COSINE").model("dense").
                corpus("simple").language("PT").mt(true).termComposition("SUM").translationComposition("AVERAGE");
        request.pairs(Arrays.asList(new TextPair("mãe", "pai"), new TextPair("mãe computador", "pai avaliação")));
        List<ScoredTextPair> relatedness = driver.getRelatedness(request);

        for (ScoredTextPair pair : relatedness) {
            Assert.assertEquals(Math.floor(pair.score), -1d);
        }
    }

    @Test
    public void getZeroRelatedness() {
        RelatednessPairRequest request = new RelatednessPairRequest().model("sparse").corpus("simple").mt(true).
                scoreFunction("COSINE").language("PT").termComposition("SUM").translationComposition("AVERAGE");
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

        RelatednessPairRequest pairRequest = new RelatednessPairRequest().scoreFunction("COSINE").model("dense").
                corpus("simple").language(lang).mt(mt).termComposition("SUM").translationComposition("AVERAGE");
        pairRequest.pairs(Arrays.asList(new TextPair("mãe", "pai"), new TextPair("mãe", "mãe computador"),
                new TextPair("mãe", "pai avaliação")));

        RelatednessOneToManyRequest otmRequest = new RelatednessOneToManyRequest().scoreFunction("COSINE").model("dense").
                corpus("simple").language(lang).mt(mt).termComposition("SUM").translationComposition("AVERAGE");
        otmRequest.one("mãe").many(Arrays.asList("pai", "mãe computador", "pai avaliação"));

        oneToManyRelatedness(pairRequest, otmRequest);
    }

    @Test
    public void oneToManyRelatednessEN() {
        String lang = "EN";
        boolean mt = false;
        RelatednessPairRequest pairRequest = new RelatednessPairRequest().scoreFunction("COSINE").model("dense").
                corpus("simple").language(lang).mt(mt);
        pairRequest.pairs(Arrays.asList(new TextPair("throne", "plane"), new TextPair("throne", "good"),
                new TextPair("throne", "hot"), new TextPair("throne", "south"), new TextPair("throne", "hate"),
                new TextPair("throne", "car"), new TextPair("throne", "bad"), new TextPair("throne", "cold"),
                new TextPair("throne", "north"), new TextPair("throne", "hot cold bad car")));

        RelatednessOneToManyRequest otmRequest = new RelatednessOneToManyRequest().scoreFunction("COSINE")
                .language(lang).mt(mt);
        otmRequest.one("throne").many(Arrays.asList("plane", "good", "hot", "south", "hate", "car", "bad", "cold",
                "north", "hot cold bad car")).model("sparse").corpus("simple");
        oneToManyRelatedness(pairRequest, otmRequest);
    }

    private void oneToManyRelatedness(RelatednessPairRequest pairRequest, RelatednessOneToManyRequest otmRequest) {
        List<ScoredTextPair> relatedness = driver.getRelatedness(pairRequest);
        Map<String, Double> pairResults = relatedness.stream().collect(Collectors.toMap(p -> p.t2, p -> p.score));

        Map<String, Double> otmRelatedness = driver.getRelatedness(otmRequest);

        for (String m : otmRelatedness.keySet()) {
            Assert.assertEquals(otmRelatedness.get(m), pairResults.get(m));
        }
    }
}
