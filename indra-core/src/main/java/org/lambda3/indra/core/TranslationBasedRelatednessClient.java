package org.lambda3.indra.core;

import org.lambda3.indra.client.*;
import org.lambda3.indra.core.translation.IndraTranslator;

import java.util.*;

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

public class TranslationBasedRelatednessClient extends RelatednessClient {
    private IndraTranslator translator;

    public TranslationBasedRelatednessClient(Params params, VectorSpace vectorSpace, RelatednessFunction func, IndraTranslator translator) {
        super(params, vectorSpace, func);
        if (translator == null) {
            throw new IllegalArgumentException("translator can't be null");
        }
        this.translator = translator;
    }

    @Override
    protected List<? extends AnalyzedPair> doAnalyze(List<TextPair> pairs) {
        logger.debug("Analyzing {} pairs", pairs.size());

        List<AnalyzedTranslatedPair> analyzedPairs = new ArrayList<>(pairs.size());
        List<MutableTranslatedTerm> analyzedTerms = new LinkedList<>();

        IndraAnalyzer analyzer = new IndraAnalyzer(params.language);

        for (TextPair pair : pairs) {
            AnalyzedTranslatedPair analyzedPair = analyzer.analyzeForTranslation(pair);
            if (analyzedPair != null) {
                analyzedPairs.add(analyzedPair);
            }

            analyzedTerms.add(analyzedPair.getTranslatedT1());
            analyzedTerms.add(analyzedPair.getTranslatedT2());
        }

        logger.debug("Translating terms..");

        if (translator != null) {
            translator.translate(analyzedTerms);
            IndraAnalyzer newLangAnalyzer = new IndraAnalyzer(translator.TARGET_LANG);

            for (MutableTranslatedTerm term : analyzedTerms) {
                Map<String, List<String>> transTokens = term.getTranslatedTokens();
                for (String token : transTokens.keySet()) {
                    term.putAnalyzedTranslatedTokens(token, newLangAnalyzer.stem(transTokens.get(token)));
                }
            }

        } else {
            logger.error("IndraTranslator is not available, but getParams().translate is true.");
        }

        return analyzedPairs;
    }

    @Override
    protected Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs) {
        return vectorSpace.getTranslatedVectorPairs((List<AnalyzedTranslatedPair>) analyzedPairs);
    }
}
