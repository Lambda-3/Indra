package org.lambda3.indra.core.utils;

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

import org.lambda3.indra.client.RelatednessRequest;
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.VectorRequest;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.composition.VectorComposition;


public class ParamsUtils {

    public static final Boolean DONT_OVERRIDE_DEFAULT_APPLY_STOPWORDS = null;
    public static final Integer DONT_OVERRIDE_DEFAULT_MIN_WORD_LENGTH = null;

    public static final VectorComposition DEFAULT_TERM_COMPOSTION = VectorComposition.UNIQUE_SUM;
    public static final VectorComposition DEFAULT_TRANSLATION_COMPOSTION = VectorComposition.AVERAGE;

    public static Params buildParams(RelatednessRequest req, boolean translate) {
        return new Params(req, translate, DEFAULT_TERM_COMPOSTION, DEFAULT_TRANSLATION_COMPOSTION);
    }

    public static Params buildParams(VectorRequest req, boolean translate) {
        return new Params(req, translate, DEFAULT_TERM_COMPOSTION, DEFAULT_TRANSLATION_COMPOSTION);
    }

    public static Params buildNoTranslateCosineDefaultParams(String corpus, String lang, String model) {
        return new Params(corpus, ScoreFunction.COSINE, lang, model, false, DONT_OVERRIDE_DEFAULT_APPLY_STOPWORDS,
                DONT_OVERRIDE_DEFAULT_MIN_WORD_LENGTH, DEFAULT_TERM_COMPOSTION, DEFAULT_TRANSLATION_COMPOSTION);
    }

    public static Params buildTranslateCosineDefaultParams(String corpus, String lang, String model) {
        return new Params(corpus, ScoreFunction.COSINE, lang, model, true, DONT_OVERRIDE_DEFAULT_APPLY_STOPWORDS,
                DONT_OVERRIDE_DEFAULT_MIN_WORD_LENGTH, DEFAULT_TERM_COMPOSTION, DEFAULT_TRANSLATION_COMPOSTION);
    }
}
