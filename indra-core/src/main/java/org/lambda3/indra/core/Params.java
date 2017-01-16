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

import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.Model;
import org.lambda3.indra.common.client.ScoreFunction;

public final class Params {
    public final String corpusName;
    public final Language language;
    public final Model model;
    public final ScoreFunction func;

    public Params(String corpusName, ScoreFunction func, Language language, Model model) {
        if (corpusName == null || func == null || language == null || model == null) {
            throw new IllegalArgumentException("All arguments are mandatory!");
        }

        this.corpusName = corpusName;
        this.func = func;
        this.language = language;
        this.model = model;
    }

    public boolean useStemming() {
        return true; //TODO: Will change or varies with the other params?
    }

    public String getDBName() {
        return String.format("%s-%s-%s",
                model.name().toLowerCase(),
                language.name().toLowerCase(),
                corpusName.toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Params params = (Params) o;

        if (!corpusName.equals(params.corpusName)) return false;
        if (language != params.language) return false;
        if (model != params.model) return false;
        return func == params.func;

    }

    @Override
    public int hashCode() {
        int result = corpusName.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + func.hashCode();
        return result;
    }

}
