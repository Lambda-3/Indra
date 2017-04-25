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

import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessResult;
import org.lambda3.indra.core.utils.ParamsUtils;
import org.lambda3.indra.mongo.MongoIndraDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MongoIndraDriverIT {
    private String mongoURI;

    @BeforeTest
    public void configure() {
        mongoURI = System.getProperty("indra.mongoURI");
        if (mongoURI == null) {
            Assert.fail("System.getProperty(\"indra.mongoURI\") is null. Provide a mongoURI to execute the integration test.");
        }
    }

    public static Params buildDefaulParams(Boolean applyStopWords, Integer minWordLength) {
        return new Params("wiki-2014", ScoreFunction.COSINE, "EN", "W2V", false, applyStopWords, minWordLength,
                ParamsUtils.DEFAULT_TERM_COMPOSTION, ParamsUtils.DEFAULT_TRANSLATION_COMPOSTION);
    }

    @Test
    public void relatednessSimpleTest() {
        Params params = ParamsUtils.buildNoTranslateCosineDefaultParams("wiki-2014", "EN", "W2V");

        MongoIndraDriver driver = new MongoIndraDriver(params, mongoURI);
        TextPair pair = new TextPair("car", "engine");

        RelatednessResult res = driver.getRelatedness(Collections.singletonList(pair));
        Assert.assertNotNull(res);
        Assert.assertEquals(1, res.getScores().size());
        ScoredTextPair scoredPair = res.getScore(pair);
        Assert.assertEquals(pair.t1, scoredPair.t1);
        Assert.assertEquals(pair.t2, scoredPair.t2);
        Assert.assertTrue(Math.abs(scoredPair.score) > 0);
    }

    @Test
    public void translatedRelatednessTest() {
        Params params = ParamsUtils.buildTranslateCosineDefaultParams("wiki-2014", "PT", "W2V");

        MongoIndraDriver driver = new MongoIndraDriver(params, mongoURI);
        List<TextPair> pairs = Arrays.asList(new TextPair("carro", "motor"), new TextPair("carro amarelo", "motor preto"));
        RelatednessResult res = driver.getRelatedness(pairs);

        Assert.assertNotNull(res);
        Assert.assertEquals(pairs.size(), res.getScores().size());
        for (TextPair pair : pairs) {
            ScoredTextPair scoredPair = res.getScore(pair);
            Assert.assertEquals(pair.t1, scoredPair.t1);
            Assert.assertEquals(pair.t2, scoredPair.t2);
            System.out.println(scoredPair.score);
            Assert.assertTrue(Math.abs(scoredPair.score) > 0d);
        }
    }

    @Test
    public void translatedZeroRelatednessTest() {
        Params params = ParamsUtils.buildTranslateCosineDefaultParams("wiki-2014", "PT", "W2V");

        MongoIndraDriver driver = new MongoIndraDriver(params, mongoURI);
        List<TextPair> pairs = Arrays.asList(new TextPair("asdfasdf", "asdfpoqw"), new TextPair("adwwwf cawerr", "asf erewr"));
        RelatednessResult res = driver.getRelatedness(pairs);

        Assert.assertNotNull(res);
        Assert.assertEquals(pairs.size(), res.getScores().size());
        for (TextPair pair : pairs) {
            ScoredTextPair scoredPair = res.getScore(pair);
            Assert.assertEquals(pair.t1, scoredPair.t1);
            Assert.assertEquals(pair.t2, scoredPair.t2);
            System.out.println(pair + ": " + scoredPair.score);
            Assert.assertEquals(scoredPair.score, 0d);
        }
    }

    @Test
    public void testMinWordLengthParam() {
        Params params = buildDefaulParams(ParamsUtils.DONT_OVERRIDE_DEFAULT_APPLY_STOPWORDS, 3);

        MongoIndraDriver driver = new MongoIndraDriver(params, mongoURI);
        List<TextPair> pairs = Arrays.asList(new TextPair("love", "romance"));
        RelatednessResult res = driver.getRelatedness(pairs);
        Assert.assertEquals(res.getScores().size(), 1);
        for (ScoredTextPair stp : res.getScores()) {
            Assert.assertTrue(Math.abs(stp.score) > 0d);
        }

        params = buildDefaulParams(ParamsUtils.DONT_OVERRIDE_DEFAULT_APPLY_STOPWORDS, 5);

        driver = new MongoIndraDriver(params, mongoURI);
        pairs = Arrays.asList(new TextPair("love", "romance"));
        res = driver.getRelatedness(pairs);
        Assert.assertEquals(res.getScores().size(), 1);
        for (ScoredTextPair stp : res.getScores()) {
            Assert.assertEquals(Math.abs(stp.score), 0d);
        }

    }

    @Test
    public void testApplyStopWordsParam() {
        //TODO when a model generated without cutting stopwords.
    }
}
