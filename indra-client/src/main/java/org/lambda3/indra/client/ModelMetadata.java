package org.lambda3.indra.client;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
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

import java.util.*;

public final class ModelMetadata {

    public static final String LOADER_ID_PARAM = "loader-id";
    private String loaderId = "legacy"; //Default is legacy for all models from the DInfra Era

    public static final String SPARSE_PARAM = "sparse";
    private boolean sparse = false;

    public static final String BINARY_PARAM = "binary";
    private boolean binary = true;

    public static final String APPLY_STEMMER_PARAM = "apply-stemmer";
    private int applyStemmer = 3;

    public static final String REMOVE_ACCENTS_PARAM = "remove-accents";
    private boolean removeAccents = true;

    public static final String APPLY_LOWERCASE_PARAM = "apply-lowercase";
    private boolean applyLowercase = true;

    public static final String APPLY_STOP_WORDS_PARAM = "apply-stop-words";
    private boolean applyStopWords = true;

    public static final String MIN_WORD_LENGTH_PARAM = "min-word-length";
    private int minWordLength = 3;

    public static final String MAX_WORD_LENGTH_PARAM = "max-word-length";
    private int maxWordLength = 100;

    public static final String DIMENSIONS_PARAM = "dimensions";
    private int dimensions = 300;

    public static final String STOP_WORDS_PARAM = "stop-words";
    private Set<String> stopWords = Collections.emptySet();

    private ModelMetadata() {
        //
    }

    private ModelMetadata(ModelMetadata other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        this.loaderId = other.loaderId;
        this.applyStemmer = other.applyStemmer;
        this.removeAccents = other.removeAccents;
        this.applyLowercase = other.applyLowercase;
        this.applyStopWords = other.applyStopWords;
        this.minWordLength = other.minWordLength;
        this.maxWordLength = other.maxWordLength;
        this.sparse = other.sparse;
        this.binary = other.binary;
        this.dimensions = other.dimensions;
        this.stopWords = new HashSet<>(other.stopWords);
    }

    public static ModelMetadata createDefault() {
        return new ModelMetadata();
    }

    public static ModelMetadata createFromMap(Map<String, Object> doc) {
        ModelMetadata metaDefault = new ModelMetadata();
        return metaDefault
                .loaderId((String) doc.getOrDefault(LOADER_ID_PARAM, metaDefault.loaderId))
                .applyStemmer((int) doc.getOrDefault(APPLY_STEMMER_PARAM, metaDefault.applyStemmer))
                .removeAccents((boolean) doc.getOrDefault(REMOVE_ACCENTS_PARAM, metaDefault.removeAccents))
                .applyLowercase((boolean) doc.getOrDefault(APPLY_LOWERCASE_PARAM, metaDefault.applyLowercase))
                .applyStopWords((boolean) doc.getOrDefault(APPLY_STOP_WORDS_PARAM, metaDefault.applyStopWords))
                .minWordLength((int) doc.getOrDefault(MIN_WORD_LENGTH_PARAM, metaDefault.minWordLength))
                .maxWordLength((int) doc.getOrDefault(MAX_WORD_LENGTH_PARAM, metaDefault.maxWordLength))
                .sparse((boolean) doc.getOrDefault(SPARSE_PARAM, metaDefault.sparse))
                .binary((boolean) doc.getOrDefault(BINARY_PARAM, metaDefault.binary))
                .dimensions((int) doc.getOrDefault(DIMENSIONS_PARAM, metaDefault.dimensions))
                .stopWords(new HashSet<>((Collection<String>) doc.getOrDefault(STOP_WORDS_PARAM, metaDefault.stopWords)));

    }

    public static ModelMetadata createTranslationVersion(ModelMetadata metadata) {
        ModelMetadata newOne = new ModelMetadata(metadata);
        return newOne.applyStemmer(0).removeAccents(false).applyLowercase(true);
    }

    public ModelMetadata loaderId(String loaderId) {
        this.loaderId = Objects.requireNonNull(loaderId);
        return this;
    }

    public ModelMetadata applyStemmer(int applyStemmer) {
        this.applyStemmer = applyStemmer;
        return this;
    }

    public ModelMetadata removeAccents(boolean removeAccents) {
        this.removeAccents = removeAccents;
        return this;
    }

    public ModelMetadata applyLowercase(boolean applyLowercase) {
        this.applyLowercase = applyLowercase;
        return this;
    }

    public ModelMetadata applyStopWords(boolean applyStopWords) {
        this.applyStopWords = applyStopWords;
        return this;
    }

    public ModelMetadata minWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
        return this;
    }

    public ModelMetadata maxWordLength(int maxWordLength) {
        this.maxWordLength = maxWordLength;
        return this;
    }

    public ModelMetadata stopWords(Set<String> stoṕWords) {
        this.stopWords = new HashSet<>(stoṕWords);
        return this;
    }

    public ModelMetadata sparse(boolean isSparse) {
        this.sparse = isSparse;
        return this;
    }

    public ModelMetadata binary(boolean isBinary) {
        this.binary = isBinary;
        return this;
    }

    public ModelMetadata dimensions(int dimensions) {
        this.dimensions = dimensions;
        return this;
    }

    public String getLoaderId() {
        return this.loaderId;
    }

    public Set<String> getStopWords() {
        return this.stopWords;
    }

    public int getDimensions() {
        return dimensions;
    }

    public boolean isBinary() {
        return binary;
    }

    public boolean isSparse() {
        return sparse;
    }

    public int getApplyStemmer() {
        return applyStemmer;
    }

    public boolean isRemoveAccents() {
        return removeAccents;
    }

    public boolean isApplyLowercase() {
        return applyLowercase;
    }

    public boolean isApplyStopWords() {
        return applyStopWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    public int getMaxWordLength() {
        return maxWordLength;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "loaderId=" + loaderId +
                ", sparse=" + sparse +
                ", binary=" + binary +
                ", applyStemmer=" + applyStemmer +
                ", applyLowercase=" + applyLowercase +
                ", removeAccents=" + removeAccents +
                ", applyStopWords=" + applyStopWords +
                ", minWordLength=" + minWordLength +
                ", maxWordLength=" + maxWordLength +
                ", dimensions=" + dimensions +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelMetadata that = (ModelMetadata) o;
        return  loaderId.equals(that.loaderId) &&
                sparse == that.sparse &&
                binary == that.binary &&
                applyStemmer == that.applyStemmer &&
                applyLowercase == that.applyLowercase &&
                removeAccents == that.removeAccents &&
                applyStopWords == that.applyStopWords &&
                minWordLength == that.minWordLength &&
                maxWordLength == that.maxWordLength &&
                dimensions == that.dimensions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(loaderId, sparse, binary, applyStemmer, applyLowercase, removeAccents, applyStopWords,
                minWordLength, maxWordLength, dimensions);
    }
}
