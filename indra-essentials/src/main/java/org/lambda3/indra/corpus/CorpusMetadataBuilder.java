package org.lambda3.indra.corpus;

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

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CorpusMetadataBuilder {

    static final String CORPUS_NAME = "corpusName";
    static final String LANGUAGE = "language";
    static final String DESCRIPTION = "description";
    static final String ENCODING = "encoding";
    static final String APPLY_STEMMER = "applyStemmer";
    static final String REMOVE_ACCENTS = "removeAccents";
    static final String APPLY_LOWERCASE = "applyLowercase";
    static final String REPLACE_NUMBERS = "replaceNumbers";
    static final String STOP_WORDS = "stopWords";
    static final String MIN_TOKEN_LENGTH = "minTokenLength";
    static final String MAX_TOKEN_LENGTH = "maxTokenLength";
    static final String TRANSFORMERS = "transformers";

    static final Map<String, Object> DEFAULT_DATA = getDefaultData();
    private Map<String, Object> data = new HashMap<>();

    private CorpusMetadataBuilder(String corpusName, String language) {
        data.put(CORPUS_NAME, corpusName);
        data.put(LANGUAGE, language);
        data.putAll(DEFAULT_DATA);
    }

    private static Map<String, Object> getDefaultData() {
        Map<String, Object> defaultData = new HashMap<>();

        defaultData.put(DESCRIPTION, null);
        defaultData.put(ENCODING, StandardCharsets.UTF_8.name());
        defaultData.put(APPLY_STEMMER, 0L);
        defaultData.put(REMOVE_ACCENTS, true);
        defaultData.put(APPLY_LOWERCASE, true);
        defaultData.put(REPLACE_NUMBERS, true);
        defaultData.put(STOP_WORDS, Collections.EMPTY_SET);
        defaultData.put(MIN_TOKEN_LENGTH, 1L);
        defaultData.put(MAX_TOKEN_LENGTH, 100L);
        defaultData.put(TRANSFORMERS, Collections.EMPTY_MAP);

        return Collections.unmodifiableMap(defaultData);
    }

    public static CorpusMetadata fromMap(Map<String, Object> map) {
        Map<String, Object> data = new HashMap<>();
        data.putAll(DEFAULT_DATA);
        data.putAll(map);
        return new CorpusMetadata(data);
    }

    public static CorpusMetadataBuilder newCorpusMetadata(String corpusName, String language) {
        return new CorpusMetadataBuilder(corpusName, language);
    }

    public CorpusMetadataBuilder desc(String desc) {
        data.put(DESCRIPTION, desc);
        return this;
    }

    public CorpusMetadataBuilder encoding(String encoding) {
        data.put(ENCODING, encoding);
        return this;
    }

    public CorpusMetadataBuilder applyStemmer(long times) {
        data.put(APPLY_STEMMER, times);
        return this;
    }

    public CorpusMetadataBuilder removeAccents(boolean removeAccents) {
        data.put(REMOVE_ACCENTS, removeAccents);
        return this;
    }

    public CorpusMetadataBuilder applyLowercase(boolean applyLowercase) {
        data.put(APPLY_LOWERCASE, applyLowercase);
        return this;
    }

    public CorpusMetadataBuilder replaceNumbers(boolean replaceNumbers) {
        data.put(REPLACE_NUMBERS, replaceNumbers);
        return this;
    }

    public CorpusMetadataBuilder stopWords(Set<String> stopWords) {
        data.put(STOP_WORDS, stopWords);
        return this;
    }

    public CorpusMetadataBuilder minTokenLength(long minTokenLength) {
        data.put(MIN_TOKEN_LENGTH, minTokenLength);
        return this;
    }

    public CorpusMetadataBuilder maxTokenLength(long maxTokenLength) {
        data.put(MAX_TOKEN_LENGTH, maxTokenLength);
        return this;
    }

    public CorpusMetadataBuilder transformers(Map<String, Collection<String>> transformers) {
        data.put(TRANSFORMERS, transformers);
        return this;
    }

    public CorpusMetadata build() {
        this.data = Collections.unmodifiableMap(data);
        return new CorpusMetadata(data);
    }

    public static CorpusMetadata random() {
        return newCorpusMetadata("bla bla", "tupi-guarani").build();
    }
}
