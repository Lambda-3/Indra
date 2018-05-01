package org.lambda3.indra.core.test;

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

import org.lambda3.indra.MutableTranslatedTerm;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MockIndraTranslator extends IndraTranslator {
    private Map<String, List<String>> translations = new HashMap<>();

    public MockIndraTranslator() {
        translations.put("mãe", Arrays.asList("mother", "mom", "matriarch"));
        translations.put("computador", Arrays.asList("machine", "computer"));
        translations.put("pai", Arrays.asList("father", "dad", "patriarch"));
        translations.put("avaliação", Arrays.asList("test", "evaluation"));
    }

    @Override
    public void translate(List<MutableTranslatedTerm> terms) {
        for (MutableTranslatedTerm term : terms) {
            for (String token : term.getAnalyzedTokens()) {
                term.putTranslatedTokens(token, translations.get(token));
            }
        }
    }

    @Override
    public CorpusMetadata loadCorpusMetadata() {
        return CorpusMetadataBuilder.newCorpusMetadata("bla", "pt").build();
    }

    @Override
    public void close() throws IOException {
        //do nothing
    }
}
