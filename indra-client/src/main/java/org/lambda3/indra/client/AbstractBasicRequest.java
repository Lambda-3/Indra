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

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class AbstractBasicRequest<T extends AbstractBasicRequest> {

    private String corpus;
    private String model;
    private String language;
    private boolean mt = false;
    private Boolean applyStopWords;
    private int minWordLength = -1; // flags user did not override the defaults
    private String termComposition;
    private String translationComposition;

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

    public T mt(boolean mt) {
        this.mt = mt;
        return (T) this;
    }

    public boolean isMt() {
        return mt;
    }

    public T applyStopWords(Boolean applyStopWords) {
        this.applyStopWords = applyStopWords;

        return (T) this;
    }

    public T minWordLength(Integer minWordLength) {
        this.minWordLength = minWordLength;
        return (T) this;
    }

    public T termComposition(String termComposition) {
        this.termComposition = termComposition;
        return (T) this;
    }

    public T translationComposition(String translationComposition) {
        this.translationComposition = translationComposition;
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

    public Integer getMinWordLength() {
        return minWordLength;
    }

    public String getTermComposition() {
        return termComposition;
    }

    public String getTranslationComposition() {
        return translationComposition;
    }

    static void checkAndAppendErrorMessages(Object object, String fieldName, StringBuilder errorMessages) {
        if (object == null) {
            errorMessages.append(" - '");
            errorMessages.append(fieldName);
            errorMessages.append("' can't be null;\\n");
        } else if ((object instanceof String && ((String) object).isEmpty()) ||
                (object instanceof List && ((List) object).isEmpty())) {
            errorMessages.append(" - '");
            errorMessages.append(fieldName);
            errorMessages.append("' can't be empty;\\n");
        }
    }

    static void checkAndAppendErrorMessagesLists(List<String> terms, String fieldName, StringBuilder errorMessages) {
        checkAndAppendErrorMessages(terms, fieldName, errorMessages);
        if (terms != null) {
            boolean valid = terms.parallelStream().allMatch(s -> s != null && !s.isEmpty());
            if (!valid) {
                errorMessages.append(" - '");
                errorMessages.append(fieldName);
                errorMessages.append("' can contain neither null nor empty strings;\\n");
            }
        }
    }

    static WebApplicationException createClientError(String msg) {
        return new BadRequestException(Response.status(400).entity(new HashMap<String, String>() {{
            put("msg", "This request contains one or more errors:\\n" + msg);
        }}).build());
    }

    /**
     * Throws an exception if this request is not in a safe state.
     *
     * @throws BadRequestException
     */
    public final void validate() {
        StringBuilder errorMessage = new StringBuilder();
        checkAndAppendErrorMessages(corpus, "corpus", errorMessage);
        checkAndAppendErrorMessages(model, "model", errorMessage);
        checkAndAppendErrorMessages(language, "language", errorMessage);

        errorMessage.append(isValid());

        if (errorMessage.length() > 0) {
            throw createClientError(errorMessage.toString());
        }
    }

    protected abstract String isValid();
}
