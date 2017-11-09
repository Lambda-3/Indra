package org.lambda3.indra;

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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MutableTranslatedTerm extends AnalyzedTerm {
    private Map<String, List<String>> translatedTokens = new HashMap<>();
    private Map<String, List<String>> analyzedTranslatedTokens = new HashMap<>();

    public MutableTranslatedTerm(String term, List<String> analyzedTokens) {
        super(term, analyzedTokens);
    }

    public Map<String, List<String>> getTranslatedTokens() {
        return translatedTokens;
    }

    public Map<String, List<String>> getAnalyzedTranslatedTokens() {
        return analyzedTranslatedTokens;
    }

    public void putTranslatedTokens(String orginalToken, List<String> translations) {
        this.translatedTokens.put(orginalToken, (translations != null ? translations : Collections.EMPTY_LIST));
    }

    public void putAnalyzedTranslatedTokens(String orginalToken, List<String> analyzedTranslations) {
        this.analyzedTranslatedTokens.put(orginalToken, (analyzedTranslations != null ? analyzedTranslations : Collections.EMPTY_LIST));
    }
}
