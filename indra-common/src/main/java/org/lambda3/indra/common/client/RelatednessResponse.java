package org.lambda3.indra.common.client;

/*-
 * ==========================License-Start=============================
 * Indra REST Module
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

import org.lambda3.indra.common.client.ScoreFunction;
import org.lambda3.indra.common.client.ScoredTextPair;

import java.util.Collection;

public final class RelatednessResponse {
    public String corpus;
    public String model;
    public String language;
    public Collection<ScoredTextPair> pairs;
    public ScoreFunction scoreFunction;

    @Override
    public String toString() {
        return "RelatednessResponse{" +
                "corpus='" + corpus + '\'' +
                ", model=" + model +
                ", language=" + language +
                ", pairs=" + pairs +
                ", scoreFunction=" + scoreFunction +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelatednessResponse that = (RelatednessResponse) o;

        if (corpus != null ? !corpus.equals(that.corpus) : that.corpus != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (pairs != null ? !pairs.equals(that.pairs) : that.pairs != null) return false;
        return scoreFunction == that.scoreFunction;
    }

    @Override
    public int hashCode() {
        int result = corpus != null ? corpus.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (pairs != null ? pairs.hashCode() : 0);
        result = 31 * result + (scoreFunction != null ? scoreFunction.hashCode() : 0);
        return result;
    }
}
