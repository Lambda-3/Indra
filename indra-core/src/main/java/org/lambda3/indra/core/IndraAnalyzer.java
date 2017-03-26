package org.lambda3.indra.core;

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

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.MutableAnalyzedTerm;
import org.lambda3.indra.client.TextPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class IndraAnalyzer {
    private static final int MIN_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 100;

    private static Logger logger = LoggerFactory.getLogger(IndraAnalyzer.class);

    private String lang;
    private boolean stemming;
    private Tokenizer tokenizer;
    private TokenStream stream;

    public IndraAnalyzer(String lang, boolean stemming) {
        if (lang == null) {
            throw new IllegalArgumentException("lang is missing");
        }
        logger.debug("Creating analyzer, lang={} (stemming={})", lang, stemming);
        this.lang = lang;
        this.stemming = stemming;
        tokenizer = new StandardTokenizer();
        stream = createStream(lang, stemming, tokenizer);
    }

    public List<String> analyze(String text) throws IOException {
        List<String> result = new ArrayList<>();
        try (StringReader reader = new StringReader(text)) {
            tokenizer.setReader(reader);
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {
                result.add(cattr.toString());
            }
        } finally {
            stream.end();
            stream.close();
        }

        return result;
    }

    AnalyzedPair analyze(TextPair pair) throws IOException {
        AnalyzedPair analyzedPair = new AnalyzedPair(pair);
        MutableAnalyzedTerm at1 = analyzedPair.getAnalyzedT1();
        MutableAnalyzedTerm at2 = analyzedPair.getAnalyzedT2();

        if (this.stemming) {
            at1.setStemmedTargetTokens(analyze(pair.t1));
            at2.setStemmedTargetTokens(analyze(pair.t2));
        } else {
            at1.setOriginalTokens(analyze(pair.t1));
            at2.setOriginalTokens(analyze(pair.t2));
        }

        return analyzedPair;
    }

    public List<String> stem(List<String> tokens) {
        //three steps of stemming for a stronger cut. The data was generated using three, so should be now.
        int numberOfSteps = 3;

        List<String> stemmed = new LinkedList<>();

        SnowballProgram stemmer = getStemmer(this.lang);
        for (String token : tokens) {
            String stemmedToken = token;

            for (int i = 0; i < numberOfSteps; i++) {
                stemmer.setCurrent(stemmedToken);
                stemmer.stem();
                stemmedToken = stemmer.getCurrent();
            }

            stemmed.add(stemmedToken);
        }

        return stemmed;
    }

    private TokenStream createStream(String lang, boolean stemming, Tokenizer tokenizer) {
        TokenStream stream = new StandardFilter(tokenizer);

        StopFilter stopFilterStream = getStopFilter(lang, stream);
        stream = stopFilterStream != null ? stopFilterStream : stream;

        if (!lang.equalsIgnoreCase("ZH") && !lang.equalsIgnoreCase("KO")) {
            stream = new LengthFilter(stream, MIN_WORD_LENGTH, MAX_WORD_LENGTH);
        }

        if (stemming) {
            stream = getStemmerFilter(lang, stream);
        }

        stream = new LowerCaseFilter(stream);
        return new ASCIIFoldingFilter(stream);
    }

    private TokenStream getStemmerFilter(String lang, TokenStream stream) {
        SnowballProgram stemmer = getStemmer(lang);

        if (stemmer != null) {
            //three steps of stemming for a stronger cut. The data was generated using three, so should be now.
            return new SnowballFilter(new SnowballFilter(new SnowballFilter(stream, stemmer), stemmer), stemmer);
        } else {
            return stream;
        }
    }

    private SnowballProgram getStemmer(String lang) {
        switch (lang.toUpperCase()) {
            case "EN":
                return new EnglishStemmer();
            case "PT":
                return new PortugueseStemmer();
            case "ES":
                return new SpanishStemmer();
            case "DE":
                return new GermanStemmer();
            case "FR":
                return new FrenchStemmer();
            case "SV":
                return new SwedishStemmer();
            case "IT":
                return new ItalianStemmer();
            case "NL":
                return new DutchStemmer();
            case "RU":
                return new RussianStemmer();

            case "AR":
            case "FA":
            case "ZH":
            case "KO":
                logger.warn("No stemmer is being used for '{}'", lang);
                return null;
        }
        return null;
    }

    private StopFilter getStopFilter(String lang, TokenStream stream) {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(lang.toLowerCase() + ".stopwords");
            if (in != null) {
                logger.debug("Loading Stop words for lang={}", lang);
                CharArraySet stopWords = new CharArraySet(30, true);
                try (BufferedReader bin = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    String[] parts;
                    while ((line = bin.readLine()) != null) {
                        parts = line.split(Pattern.quote("|"));
                        line = parts[0].trim();

                        if (line.length() > 0) {
                            stopWords.add(line);
                        }
                    }

                }
                return new StopFilter(stream, stopWords);
            } else {
                logger.warn("No stop words found for lang={}", lang);
            }
        } catch (Exception e) {
            logger.error("Error creating stop filter for lang={}", lang, e);
        }
        return null;
    }
}
