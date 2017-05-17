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

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Objects;

public abstract class AbstractBasicRequest<T extends AbstractBasicRequest> {

    private String corpus;
    private String model;
    private String language;
    private Boolean applyStopWords;
    private int minWordLength;

    public T corpus(String corpusName) {
        this.corpus = Objects.requireNonNull(corpusName);
        return (T) this;
    }

    public T model(String model) {
        this.model = Objects.requireNonNull(model);
        return (T) this;
    }

    public T language(String language) {
        this.language = Objects.requireNonNull(language);
        return (T) this;
    }

    public T applyStopWords(Boolean applyStopWords) {
        this.applyStopWords = applyStopWords;
        return (T) this;
    }

    public T minWordLength(int minWordLength) {
        this.minWordLength = minWordLength;
        return (T) this;
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


    public Boolean getApplyStopWords() {
        return applyStopWords;
    }

    public int getMinWordLength() {
        return minWordLength;
    }

    /**
     * Throws an exception if this request is not in a safe state.
     * @throws WebApplicationException
     */
    public final void validate() {
        boolean invalid = corpus == null || corpus.isEmpty() || model == null || model.isEmpty() ||
                language == null || language.isEmpty() || minWordLength < 0 || !isValid();

        if (invalid) {
            throw new WebApplicationException("Invalid Indra Request", Response.Status.BAD_REQUEST);
        }
    }

    protected abstract boolean isValid();
}
