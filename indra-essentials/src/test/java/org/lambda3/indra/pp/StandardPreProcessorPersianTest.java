package org.lambda3.indra.pp;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
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

import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class StandardPreProcessorPersianTest {
    private String content = "فارسی یا پارسی ی ي  یکی از زبان\u200Cهای هندواروپایی در شاخهٔ زبان\u200Cهای ایرانی جنوب غربی است" +
            " که در کشورهای ایران٬ افغانستان،[۳] تاجیکستان[4] و ازبکستان[۵] به آن سخن می\u200Cگویند." +
            " فارسی زبان رسمی کشورهای ایران و تاجیکستان و یکی از دو زبان٬ًٍ رسمی افغانستان (در کنار پشتو) است." +
            " زبان رسمی کشور هندوستان نیز تا پیش از ورود استعمار انگلیس، فارسی بود.[۶]";


    @Test(enabled = false)
    public void stemmerTest() {

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                applyLowercase(true).removeAccents(true).applyStemmer(1).build();

        String word = "کتاب\u200Cها";

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(word));
        // https://github.com/htaghizadeh/PersianStemmer-Java

        Assert.assertFalse(processedContent.contains("کتاب\u200Cها"));
        Assert.assertTrue(processedContent.contains("کتاب"));

    }

    @Test
    public void keepAlphaNumTokensTest() {

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                applyLowercase(true).removeAccents(true).applyStemmer(0).build();

        String content = " 2سیامک34";

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertFalse(processedContent.contains("2 سیامک 34"));
        Assert.assertTrue(processedContent.contains("2سیامک34"));

    }

    @Test(enabled = false)
    public void accentTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").removeAccents(true).applyStemmer(0).build();
        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));
        //TODO please implement me.
    }

    @Test(enabled = false)
    public void transformerTest() {
        List<String> mwt = Arrays.asList("فارسی زبان رسمی کشورهای ایران", "زبان رسمی کشور هندوستان",
                "که در کشورهای ایران٬ افغانستان،[۳]");

        Map<String, Collection<String>> transformers = Collections.singletonMap("MultiWordsTransformer", mwt);

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                applyLowercase(true).removeAccents(true).applyStemmer(0).transformers(transformers).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertFalse(processedContent.contains("فارسی زبان رسمی کشورهای ایران"));
        Assert.assertTrue(processedContent.contains("فارسی_زبان_رسمی_کشورهای_ایران"));

        Assert.assertFalse(processedContent.contains("زبان رسمی کشور هندوستان"));
        Assert.assertTrue(processedContent.contains("زبان_رسمی_کشور_هندوستان"));
        Assert.assertFalse(processedContent.contains("که در کشورهای ایران افغانستان ۳"));
        Assert.assertTrue(processedContent.contains("که_در_کشورهای_ایران__افغانستان__۳"));
    }

    @Test
    public void stopWordsTest() {
        Set<String> stopWords = new HashSet<>(Arrays.asList("یکی", "در", "یا"));

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                applyLowercase(true).removeAccents(true).applyStemmer(0).stopWords(stopWords).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertTrue(processedContent.startsWith("فارسی"));
        Assert.assertFalse(processedContent.startsWith("فارسی یا پارسی"));
        Assert.assertTrue(processedContent.startsWith("فارسی پارسی"));

        Assert.assertFalse(processedContent.contains("در"));
        Assert.assertFalse(processedContent.contains("یکی"));
    }

    @Test(enabled = false)
    public void keepNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                replaceNumbers(false).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        // Convert Persian & Arabic number to English

        Assert.assertTrue(processedContent.contains("4"));
        Assert.assertTrue(processedContent.contains("۳"));
        Assert.assertTrue(processedContent.contains("۵"));
        Assert.assertTrue(processedContent.contains("۶"));
        Assert.assertFalse(processedContent.contains("<NUMBER>"));

    }

    @Test(enabled = false)
    public void removeNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                replaceNumbers(true).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        // Convert Persian & Arabic number to English

        Assert.assertFalse(processedContent.contains("4"));
        Assert.assertFalse(processedContent.contains("۳"));
        Assert.assertFalse(processedContent.contains("۵"));
        Assert.assertFalse(processedContent.contains("۶"));
        Assert.assertTrue(processedContent.contains("<NUMBER>"));
    }

    @Test
    public void removeFloatNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "fa").
                replaceNumbers(true).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process("How money is written in English, $30.50 or $30,50?"));

        //TODO write this one here.
    }
}


