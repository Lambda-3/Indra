package org.lambda3.indra.client;

import java.util.Objects;

public class AbstractBasicRequest<T extends AbstractBasicRequest> {

    private String corpus;
    private String model;
    private String language;
    private Boolean applyStopWords;
    private Integer minWordLength;

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

    public T minWordLength(Integer minWordLength) {
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

    public Integer getMinWordLength() {
        return minWordLength;
    }
}
