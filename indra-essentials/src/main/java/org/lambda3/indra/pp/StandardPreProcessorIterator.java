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

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.exception.IndraException;
import org.lambda3.indra.exception.IndraRuntimeException;
import org.lambda3.indra.pp.transform.MultiWordsTransformer;
import org.lambda3.indra.pp.transform.Transformer;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class StandardPreProcessorIterator implements Iterator<String> {
    public static final String NUMBER_PLACEHOLDER = "<NUMBER>";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+([,\\.][0-9]+)*$");

    private Tokenizer tokenizer;
    private TokenStream tokenStream;
    private CorpusMetadata metadata;
    private CharTermAttribute cattr;
    private List<Transformer> transformers = new LinkedList<>();

    StandardPreProcessorIterator(CorpusMetadata metadata, String text) {
        this.metadata = Objects.requireNonNull(metadata);
        ;
        this.tokenizer = new StandardTokenizer();
        this.tokenStream = createStream(metadata, tokenizer);
        setTransformers();
        initialize(Objects.requireNonNull(text));
    }

    private void setTransformers() {
        for (String trans : metadata.transformers.keySet()) {
            if (trans.equalsIgnoreCase(MultiWordsTransformer.class.getSimpleName())) {
                transformers.add(new MultiWordsTransformer(metadata.transformers.get(trans)));
            } else {
                throw new RuntimeException("Transformer not supported = " + trans);
            }
        }
    }

    private void initialize(String text) {
        String content = metadata.applyLowercase ? text.toLowerCase() : text;

        if (!transformers.isEmpty()) {
            StringBuilder sbContent = new StringBuilder(content);
            transformers.forEach(t -> t.transform(sbContent));
            content = sbContent.toString();
        }

        StringReader reader = new StringReader(content);

        tokenizer.setReader(reader);
        this.cattr = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
        } catch (IOException e) {
            String initialPart = text.substring(0, Math.min(30, text.length()));
            throw new IndraRuntimeException(String.format("Error parsing the input starting with '%s'...", initialPart), e);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return tokenStream.incrementToken();
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public String next() {
        return cattr.toString();
    }

    private TokenStream getStopFilter(String lang, Set<String> metadataStopWords, TokenStream stream) {

        if (metadataStopWords != null && !metadataStopWords.isEmpty()) {
            return new StopFilter(stream, new CharArraySet(metadataStopWords, false));

        } else {
            try {
                Set<String> sws = getDefaultStopWordSet(lang);

                if (sws != null) {
                    CharArraySet stopWords = new CharArraySet(30, true);
                    stopWords.addAll(sws);
                    return new StopFilter(stream, stopWords);
                }
            } catch (IndraException e) {
                throw new IndraRuntimeException(String.format("Error creating stop filter for lang '%s'", lang), e);
            }
        }
        return stream;
    }

    public static Set<String> getDefaultStopWordSet(String lang) throws IndraException {

        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(lang.toLowerCase() + ".stopwords");
            if (in != null) {
                Set<String> stopWords = new HashSet<>();
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
                    return stopWords;
                }
            }
        } catch (Exception e) {
            throw new IndraException(String.format("Error creating stop filter for lang '%s'", lang), e);
        }
        return null;
    }

    private TokenStream createStream(CorpusMetadata metadata, Tokenizer tokenizer) {
        TokenStream stream = new StandardFilter(tokenizer);
        stream = new LengthFilter(stream, (int) metadata.minTokenLength, (int) metadata.maxTokenLength);

        if (!metadata.stopWords.isEmpty()) {
            stream = getStopFilter(metadata.language, metadata.stopWords, stream);
        }

        if (metadata.applyStemmer > 0) {
            stream = getStemmerFilter(metadata.language, (int) metadata.applyStemmer, stream);
        }

        if (metadata.removeAccents) {
            stream = new ASCIIFoldingFilter(stream);
        }

        if (metadata.replaceNumbers) {
            stream = new PatternReplaceFilter(stream, NUMBER_PATTERN, NUMBER_PLACEHOLDER, false);
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

    public static SnowballProgram getStemmer(String lang) {
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
                return null;
        }
        return null;
    }
}
