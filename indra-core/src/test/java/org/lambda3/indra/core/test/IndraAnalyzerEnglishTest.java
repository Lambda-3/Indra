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

import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.lambda3.indra.pp.StandardPreProcessorIterator;
import org.tartarus.snowball.SnowballProgram;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class IndraAnalyzerEnglishTest {

    private static final String LANG = "EN";

    public CorpusMetadataBuilder builder() {
        return CorpusMetadataBuilder.newCorpusMetadata("bla bla", LANG);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void nullStringTest() {
        CorpusMetadata metadata = builder().build();
        IndraAnalyzer analyzer = new IndraAnalyzer(metadata);
        analyzer.analyze(null);
    }

    @Test
    public void nonStemmedAnalyzeTest() {
        CorpusMetadata metadata = builder().build();
        IndraAnalyzer analyzer = new IndraAnalyzer(metadata);
        String loveString = "love";
        List<String> res = analyzer.analyze(loveString);

        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0), loveString);

        loveString = "LOVE";
        res = analyzer.analyze(loveString);

        Assert.assertEquals(res.size(), 1);
        Assert.assertEquals(res.get(0), loveString.toLowerCase());
    }

    @Test
    public void stemmedAnalyzeTest() {
        final int TIMES = 3;
        CorpusMetadata metadata = builder().applyStemmer(TIMES).build();
        IndraAnalyzer analyzer = new IndraAnalyzer(metadata);

        String term = "hapiness";
        List<String> res = analyzer.analyze(term);
        Assert.assertEquals(res.size(), 1);


        SnowballProgram stemmer = StandardPreProcessorIterator.getStemmer(LANG);
        stemmer.setCurrent(term);
        for (int i = 0; i < TIMES; i++) {
            stemmer.stem();
        }

        Assert.assertEquals(res.get(0), stemmer.getCurrent());
    }

    @Test
    public void expressionAnalyzeTest() {
        CorpusMetadata metadata = builder().minTokenLength(3).build();
        IndraAnalyzer analyzer = new IndraAnalyzer(metadata);

        String term = "GIANT by thine OWN nature";
        List<String> res = analyzer.analyze(term);

        Assert.assertEquals(res.size(), 4);
        Assert.assertEquals(String.join(" ", res), term.toLowerCase().replace(" by", ""));
    }

    @Test
    public void analyzeMultiplesRequestsTest() {
        List<String> terms = Arrays.asList("love", "hapiness", "information", "management", "language",
                "strawberries", "refactorization");

        CorpusMetadata metadata = builder().build();
        IndraAnalyzer analyzer = new IndraAnalyzer(metadata);

        for (String term : terms) {
            List<String> res = analyzer.analyze(term);
            Assert.assertEquals(res.size(), 1);
        }

        metadata = builder().build();
        analyzer = new IndraAnalyzer(metadata);
        for (String term : terms) {
            List<String> res = analyzer.analyze(term);
            Assert.assertEquals(res.size(), 1);
        }
    }
}
