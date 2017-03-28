package org.lambda3.indra.core.tests;

import org.lambda3.indra.core.IndraAnalyzer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

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
public class IndraAnalyzerEnglishTest {
    @Test
    public void simpleNonStemmedAnalyzeTest() {
        IndraAnalyzer analyzer = new IndraAnalyzer("EN");
        String loveString = "love";
        List<String> res = analyzer.nonStemmedAnalyze(loveString);

        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0), loveString);
    }

    @Test
    public void upperCaseNonStemmedAnalyzeTest() {
        IndraAnalyzer analyzer = new IndraAnalyzer("EN");
        String loveString = "LOVE";
        List<String> res = analyzer.nonStemmedAnalyze(loveString);

        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0), loveString.toLowerCase());
    }

    @Test
    public void stemmedAnalyzeTest() {
        IndraAnalyzer analyzer = new IndraAnalyzer("EN");
        String term = "hapiness";
        List<String> res = analyzer.stemmedAnalyze(term);
        Assert.assertEquals(res.size(), 1);

        List<String> stems = analyzer.stem(Arrays.asList(term));
        Assert.assertEquals(stems.size(), 1);
        Assert.assertEquals(res.get(0), stems.get(0));
    }

    @Test
    public void expressionAnalyzeTest() {
        IndraAnalyzer analyzer = new IndraAnalyzer("EN");
        String term = "GIANT by thine OWN nature";
        List<String> res = analyzer.nonStemmedAnalyze(term);
        //'by' is ignored by the LengthFilter (min=3) and 'own' is ignored by the StopFilter (stopword).
        Assert.assertEquals(res.size(), 3);
        Assert.assertEquals(String.join(" ", res), term.toLowerCase().replace(" by", "").replace(" own", ""));
    }

    @Test
    public void analyzeMultiplesRequestsTest() {
        IndraAnalyzer analyzer = new IndraAnalyzer("EN");
        List<String> terms = Arrays.asList("love", "hapiness", "information", "management", "language",
                "strawberries", "refactorization");

        for(String term : terms) {
            List<String> res = analyzer.nonStemmedAnalyze(term);
            Assert.assertEquals(res.size(), 1);
        }

        for(String term : terms) {
            List<String> res = analyzer.stemmedAnalyze(term);
            Assert.assertEquals(res.size(), 1);
        }
    }
}
