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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelMetadata that = (ModelMetadata) o;

        if (applyStemmer != that.applyStemmer) return false;
        if (removeAccents != that.removeAccents) return false;
        if (applyStopWords != that.applyStopWords) return false;
        if (minWordLength != that.minWordLength) return false;
        return maxWordLength == that.maxWordLength;

    }

    @Override
    public int hashCode() {
        int result = (applyStemmer ? 1 : 0);
        result = 31 * result + (removeAccents ? 1 : 0);
        result = 31 * result + (applyStopWords ? 1 : 0);
        result = 31 * result + minWordLength;
        result = 31 * result + maxWordLength;
        return result;
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
}
