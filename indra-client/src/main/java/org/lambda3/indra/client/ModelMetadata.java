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

import java.util.Objects;

public class ModelMetadata {

    public static final int DEFAULT_MIN_WORD_LENGTH = 3;
    public static final int DEFAULT_MAX_WORD_LENGTH = 100;

    private boolean applyStemmer = true;
    private boolean removeAccents = true;
    private boolean applyStopWords = true;
    private int minWordLength = DEFAULT_MIN_WORD_LENGTH;
    private int maxWordLength = DEFAULT_MAX_WORD_LENGTH;

    private ModelMetadata() {

    }

    private ModelMetadata(ModelMetadata other) {
        if (other == null) {
            throw new IllegalArgumentException();
        }
        this.applyStemmer = other.applyStemmer;
        this.removeAccents = other.removeAccents;
        this.applyStopWords = other.applyStopWords;
        this.minWordLength = other.minWordLength;
        this.maxWordLength = other.maxWordLength;
    }

    public static ModelMetadata createDefault() {
        return new ModelMetadata();
    }

    public static ModelMetadata createTranslationVersion(ModelMetadata metadata) {
        ModelMetadata newOne = new ModelMetadata(metadata);
        return newOne.applyStemmer(false).removeAccents(false);
    }

    public ModelMetadata applyStemmer(boolean applyStemmer) {
        this.applyStemmer = applyStemmer;
        return this;
    }

    public ModelMetadata removeAccents(boolean removeAccents) {
        this.removeAccents = removeAccents;
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

    public boolean isApplyStemmer() {
        return applyStemmer;
    }

    public boolean isRemoveAccents() {
        return removeAccents;
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
        return "ModelMetadata{" +
                "applyStemmer=" + applyStemmer +
                ", removeAccents=" + removeAccents +
                ", applyStopWords=" + applyStopWords +
                ", minWordLength=" + minWordLength +
                ", maxWordLength=" + maxWordLength +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelMetadata that = (ModelMetadata) o;
        return applyStemmer == that.applyStemmer &&
                removeAccents == that.removeAccents &&
                applyStopWords == that.applyStopWords &&
                minWordLength == that.minWordLength &&
                maxWordLength == that.maxWordLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(applyStemmer, removeAccents, applyStopWords, minWordLength, maxWordLength);
    }
}
