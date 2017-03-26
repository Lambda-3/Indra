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

import org.lambda3.indra.client.ScoreFunction;

import java.util.Objects;

public final class Params {
    public final String corpusName;
    public final ScoreFunction func;
    public final String language;
    public final String model;
    public final boolean translate;

    public Params(String corpusName, ScoreFunction func, String language, String model) {
        this(corpusName, func, language, model, false);
    }

    public Params(String corpusName, ScoreFunction func, String language, String model, boolean translate) {
        if (corpusName == null || func == null || language == null || model == null) {
            throw new IllegalArgumentException("All arguments are mandatory!");
        }

        this.corpusName = corpusName;
        this.func = func;
        this.language = language;
        this.model = model;
        this.translate = translate;
    }

    public String getDBName() {
        return String.format("%s-%s-%s",
                model.toLowerCase(),
                language.toLowerCase(),
                corpusName.toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Params params = (Params) o;
        return Objects.equals(corpusName, params.corpusName) &&
                Objects.equals(language, params.language) &&
                Objects.equals(model, params.model) &&
                func == params.func;
    }

    @Override
    public int hashCode() {
        return Objects.hash(corpusName, language, model, func);
    }
}
