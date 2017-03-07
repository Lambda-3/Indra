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

import java.util.List;
import java.util.Objects;

public final class RelatednessRequest {

    private String corpus;
    private String model;
    private String language;
    private List<TextPair> pairs;
    private ScoreFunction scoreFunction;

    public RelatednessRequest corpus(String corpusName) {
        this.corpus = Objects.requireNonNull(corpusName);
        return this;
    }

    public RelatednessRequest model(String model) {
        this.model = Objects.requireNonNull(model);
        return this;
    }

    public RelatednessRequest language(String language) {
        this.language = Objects.requireNonNull(language);
        return this;
    }

    public RelatednessRequest scoreFunction(ScoreFunction scoreFunction) {
        this.scoreFunction = Objects.requireNonNull(scoreFunction);
        return this;
    }

    public RelatednessRequest pairs(List<TextPair> pairs) {
        this.pairs = Objects.requireNonNull(pairs);
        return this;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getModel() {
        return model;
    }

    public String getLanguage() {
        return language;
    }

    public List<TextPair> getPairs() {
        return pairs;
    }

    public ScoreFunction getScoreFunction() {
        return scoreFunction;
    }


    @Override
    public String toString() {
        return "RelatednessRequest{" +
                "corpus='" + corpus + '\'' +
                ", model=" + model +
                ", language=" + language +
                ", pairs=" + pairs +
                ", scoreFunction=" + scoreFunction +
                '}';
    }
}
