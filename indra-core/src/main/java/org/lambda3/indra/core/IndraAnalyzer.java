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

import org.apache.lucene.analysis.CharArraySet;
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
import org.lambda3.indra.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public final class IndraAnalyzer {

    private static Logger logger = LoggerFactory.getLogger(IndraAnalyzer.class);

    private Tokenizer tokenizer;
    private TokenStream tokenStream;


    public IndraAnalyzer(String lang, ModelMetadata metadata) {
        if (lang == null || metadata == null) {
            throw new IllegalArgumentException("all parameters are mandatory.");
        }

        logger.debug("Creating analyzer, lang={}, preprocessing={}", lang, metadata);
        tokenizer = new StandardTokenizer();
        tokenStream = createStream(lang, metadata, tokenizer);
    }

    @SuppressWarnings("unchecked")
    public <T extends AnalyzedPair> T analyze(TextPair pair, Class<T> clazz) {
        if (clazz.equals(AnalyzedTranslatedPair.class)) {
            AnalyzedTranslatedPair analyzedPair = new AnalyzedTranslatedPair(pair);
            analyzedPair.setTranslatedTerm1(new MutableTranslatedTerm(pair.t1, analyze(pair.t1)));
            analyzedPair.setTranslatedTerm2(new MutableTranslatedTerm(pair.t2, analyze(pair.t2)));
            return (T) analyzedPair;
        }

        AnalyzedPair analyzedPair = new AnalyzedPair(pair);
        analyzedPair.setAnalyzedTerm1(new AnalyzedTerm(pair.t1, analyze(pair.t1)));
        analyzedPair.setAnalyzedTerm2(new AnalyzedTerm(pair.t2, analyze(pair.t2)));

        return (T) analyzedPair;
    }

    public List<String> analyze(String text) {
        if (text == null) {
            return null;
        }

        List<String> result = new ArrayList<>();
        try (StringReader reader = new StringReader(text)) {
            tokenizer.setReader(reader);
            CharTermAttribute cattr = tokenStream.addAttribute(CharTermAttribute.class);
            tokenStream.reset();
            while (tokenStream.incrementToken()) {
                result.add(cattr.toString());
            }
        } catch (IOException e) {
            logger.error("Error analyzing {}", text, e);
        } finally {
            try {
                tokenStream.end();
                tokenStream.close();
            } catch (IOException e) {
                logger.error("Error closing stream {}", e);
            }
        }

        return result;
    }

    private TokenStream createStream(String lang, ModelMetadata metadata, Tokenizer tokenizer) {
        TokenStream stream = new StandardFilter(tokenizer);
        stream = new LengthFilter(stream, metadata.getMinWordLength(), metadata.getMaxWordLength());

        if (metadata.isApplyLowercase()) {
            stream = new LowerCaseFilter(stream);
        }

        if (metadata.isApplyStopWords()) {
            stream = getStopFilter(lang, metadata.getStopWords(), stream);
        }

        if (metadata.getApplyStemmer() > 0) {
            stream = getStemmerFilter(lang, metadata.getApplyStemmer(), stream);
        }

        if (metadata.isRemoveAccents()) {
            stream = new ASCIIFoldingFilter(stream);
        }

        return stream;
    }

    private TokenStream getStemmerFilter(String lang, int times, TokenStream stream) {
        SnowballProgram stemmer = getStemmer(lang);

        if (stemmer != null && times > 0) {
            for (int i = 0; i < times; i++) {
                stream = new SnowballFilter(stream, stemmer);
            }
        }

        return stream;
    }

    private static SnowballProgram getStemmer(String lang) {
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

    public static List<String> stem(Collection<String> tokens, String lang, int times) {

        List<String> stemmed = new LinkedList<>();

        SnowballProgram stemmer = getStemmer(lang);
        for (String token : tokens) {
            String stemmedToken = token;

            for (int i = 0; i < times; i++) {
                stemmer.setCurrent(stemmedToken);
                stemmer.stem();
                stemmedToken = stemmer.getCurrent();
            }

            stemmed.add(stemmedToken);
        }

        return stemmed;
    }

    private TokenStream getStopFilter(String lang, Set<String> metadataStopWords, TokenStream stream) {

        if (metadataStopWords != null && !metadataStopWords.isEmpty()) {
            return new StopFilter(stream, new CharArraySet(metadataStopWords, false));

        } else {
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
                        return new StopFilter(stream, stopWords);
                    }
                } else {
                    logger.warn("No stop words found for lang={}", lang);
                }
            } catch (Exception e) {
                logger.error("Error creating stop filter for lang={}", lang, e);
            }
        }

        return stream;
    }
}
