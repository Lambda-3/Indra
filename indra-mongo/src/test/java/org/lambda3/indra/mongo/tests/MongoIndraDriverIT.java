package org.lambda3.indra.mongo.tests;

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

import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessResult;
import org.lambda3.indra.core.utils.ParamsUtils;
import org.lambda3.indra.mongo.MongoIndraDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MongoIndraDriverIT {

    @Test
    public void relatednessSimpleTest() {
        Params params = ParamsUtils.buildNoTranslateCosineDefaultParams("wiki-2014", "EN", "W2V");
        String mongoURI = "mongodb://132.231.141.167:27017";

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
        String mongoURI = "mongodb://132.231.141.167:27017";

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
            Assert.assertTrue(Math.abs(scoredPair.score) > 0);
        }
    }

    @Test
    public void translatedZeroRelatednessTest() {
        Params params = ParamsUtils.buildTranslateCosineDefaultParams("wiki-2014", "PT", "W2V");
        String mongoURI = "mongodb://132.231.141.167:27017";

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
}
