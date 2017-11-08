package org.lambda3.indra.mongo.test;

/*-
 * ==========================License-Start=============================
 * Indra Mongo Module
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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import org.lambda3.indra.ScoredTextPair;
import org.lambda3.indra.TextPair;
import org.lambda3.indra.mongo.MongoIndraDriver;
import org.lambda3.indra.mongo.MongoTranslatorFactory;
import org.lambda3.indra.mongo.MongoVectorSpaceFactory;
import org.lambda3.indra.request.RelatednessPairRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

public final class MongoIndraDriverIT {
    private MongoVectorSpaceFactory vectorSpaceFactory;
    private MongoTranslatorFactory translatorFactory;

    @BeforeTest
    private void configure() {
        final String mongoURI = System.getProperty("indra.mongoURI");
        if (mongoURI == null) {
            Assert.fail("System.getProperty(\"indra.mongoURI\") is null. Provide a mongoURI to execute the integration test.");
        }

        MongoClientOptions builder = MongoClientOptions.builder().serverSelectionTimeout(5000).build();
        MongoClient mongoClient = new MongoClient(mongoURI, builder);
        vectorSpaceFactory = new MongoVectorSpaceFactory(mongoClient);
        translatorFactory = new MongoTranslatorFactory(mongoClient);
    }

    private static RelatednessPairRequest buildDefaulRequest() {
        return new RelatednessPairRequest().corpus("wiki-2014").scoreFunction("COSINE").language("EN").
                model("W2V").mt(false);
    }

    @Test
    public void relatednessSimpleTest() {
        RelatednessPairRequest request = buildDefaulRequest();
        TextPair pair = new TextPair("car", "engine");
        request.pairs(Collections.singletonList(pair));

        MongoIndraDriver driver = new MongoIndraDriver(vectorSpaceFactory, translatorFactory);
        List<ScoredTextPair> res = driver.getRelatedness(request);
        Assert.assertNotNull(res);
        Assert.assertEquals(1, res.size());
        ScoredTextPair scoredPair = res.iterator().next();
        Assert.assertEquals(pair.t1, scoredPair.t1);
        Assert.assertEquals(pair.t2, scoredPair.t2);
        Assert.assertTrue(Math.abs(scoredPair.score) > 0);
    }

    @Test
    public void translatedRelatednessTest() {
        MongoIndraDriver driver = new MongoIndraDriver(vectorSpaceFactory, translatorFactory);
        RelatednessPairRequest request = buildDefaulRequest().mt(true).language("PT");
        List<TextPair> pairs = Arrays.asList(new TextPair("carro", "motor"), new TextPair("carro amarelo", "motor preto"));
        request.pairs(pairs);

        Map<Integer, TextPair> mapPairs = new HashMap<>();
        pairs.forEach(p -> mapPairs.put(Objects.hash(p.t1, p.t2), p));

        List<ScoredTextPair> res = driver.getRelatedness(request);

        Assert.assertNotNull(res);
        Assert.assertEquals(pairs.size(), res.size());


        for (ScoredTextPair scoredPair : res) {
            TextPair textPair = mapPairs.get(Objects.hash(scoredPair.t1, scoredPair.t2));

            Assert.assertNotNull(textPair);
            System.out.println(textPair + ": " + scoredPair.score);
            Assert.assertTrue(Math.abs(scoredPair.score) > 0d);
        }
    }

    @Test
    public void translatedZeroRelatednessTest() {
        MongoIndraDriver driver = new MongoIndraDriver(vectorSpaceFactory, translatorFactory);
        RelatednessPairRequest request = buildDefaulRequest().mt(true).language("PT");
        List<TextPair> pairs = Arrays.asList(new TextPair("asdfasdf", "asdfpoqw"), new TextPair("adwwwf cawerr", "asf erewr"));
        request.pairs(pairs);

        Map<Integer, TextPair> mapPairs = new HashMap<>();
        pairs.forEach(p -> mapPairs.put(Objects.hash(p.t1, p.t2), p));

        List<ScoredTextPair> res = driver.getRelatedness(request);

        Assert.assertNotNull(res);
        Assert.assertEquals(pairs.size(), res.size());
        for (ScoredTextPair scoredPair : res) {
            TextPair textPair = mapPairs.get(Objects.hash(scoredPair.t1, scoredPair.t2));

            Assert.assertNotNull(textPair);
            System.out.println(textPair + ": " + scoredPair.score);
            Assert.assertEquals(scoredPair.score, 0d);
        }
    }

    @Test
    public void testMinWordLengthParam() {
        MongoIndraDriver driver = new MongoIndraDriver(vectorSpaceFactory, translatorFactory);
        RelatednessPairRequest request = buildDefaulRequest().minWordLength(3).termComposition("SUM").
                translationComposition("AVERAGE");
        List<TextPair> pairs = Collections.singletonList(new TextPair("love", "romance"));
        request.pairs(pairs);

        List<ScoredTextPair> res = driver.getRelatedness(request);
        Assert.assertEquals(res.size(), 1);
        for (ScoredTextPair stp : res) {
            Assert.assertTrue(Math.abs(stp.score) > 0d);
        }

        driver = new MongoIndraDriver(vectorSpaceFactory, translatorFactory);
        request = buildDefaulRequest().minWordLength(5);
        pairs = Collections.singletonList(new TextPair("love", "romance"));
        request.pairs(pairs);

        res = driver.getRelatedness(request);
        Assert.assertEquals(res.size(), 1);
        for (ScoredTextPair stp : res) {
            Assert.assertEquals(Math.abs(stp.score), 0d);
        }

    }

    @Test(enabled = false)
    public void testApplyStopWordsParam() {
        //TODO to a model generated without removing stopwords.
    }
}
