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


import java.util.Map;

public final class VectorResponse {

    private String corpus;
    private String model;
    private String language;
    private Map<String, ?> terms;

    private VectorResponse() {
        //jersey demands.
    }

    public VectorResponse(VectorRequest request) {
        this.corpus = request.getCorpus();
        this.model = request.getModel();
        this.language = request.getLanguage();
    }

    public void setDenseVectors(Map<String, double[]> terms) {
        this.terms = terms;
    }

    public void setSparseVectors(Map<String, Map<Integer, Double>> terms) {
        this.terms = terms;
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

    public Map<String, Map<Integer, Double>> getSparseVectors() {
        try {
            return (Map<String, Map<Integer, Double>>) terms;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public Map<String, double[]> getDenseVectors() {
        try {
            return (Map<String, double[]>) terms;
        } catch (ClassCastException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "VectorResponse{" +
                "corpus='" + corpus + '\'' +
                ", model='" + model + '\'' +
                ", language='" + language + '\'' +
                ", terms=" + terms +
                '}';
    }
}
