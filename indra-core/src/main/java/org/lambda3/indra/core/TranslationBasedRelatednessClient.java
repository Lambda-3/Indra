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

import org.lambda3.indra.client.*;
import org.lambda3.indra.entity.composition.VectorComposer;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.vs.VectorSpace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class TranslationBasedRelatednessClient extends RelatednessClient {
    private IndraTranslator translator;

    TranslationBasedRelatednessClient(IndraTranslator translator, RelatednessRequest request, VectorSpace vectorSpace) {
        super(request, vectorSpace);
        if (translator == null) {
            throw new IllegalArgumentException("translator can't be null");
        }
        this.translator = translator;
    }


    private void translate(List<MutableTranslatedTerm> analyzedTerms) {
        logger.debug("Translating terms.");

        if (translator != null) {
            translator.translate(analyzedTerms);

            for (MutableTranslatedTerm term : analyzedTerms) {
                Map<String, List<String>> transTokens = term.getTranslatedTokens();
                for (String token : transTokens.keySet()) {
                    term.putAnalyzedTranslatedTokens(token, IndraAnalyzer.stem(transTokens.get(token),
                            IndraTranslator.DEFAULT_TRANSLATION_TARGET_LANGUAGE, vectorSpace.getMetadata().getApplyStemmer()));
                }
            }

        } else {
            logger.error("IndraTranslator is not available, but getParams().translate is true.");
        }
    }

    @Override
    protected List<AnalyzedPair> doAnalyzePairs(List<TextPair> pairs) {
        logger.debug("Analyzing {} pairs", pairs.size());

        List<AnalyzedPair> analyzedPairs = new ArrayList<>(pairs.size());
        List<MutableTranslatedTerm> analyzedTerms = new LinkedList<>();

        IndraAnalyzer analyzer = new IndraAnalyzer(request.getLanguage(),
                ModelMetadata.createTranslationVersion(vectorSpace.getMetadata()));

        for (TextPair pair : pairs) {
            AnalyzedTranslatedPair analyzedPair = analyzer.analyze(pair, AnalyzedTranslatedPair.class);

            analyzedPairs.add(analyzedPair);

            analyzedTerms.add(analyzedPair.getTranslatedT1());
            analyzedTerms.add(analyzedPair.getTranslatedT2());
        }

        translate(analyzedTerms);
        return analyzedPairs;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<AnalyzedTerm> doAnalyze(String one, List<String> terms) {
        IndraAnalyzer analyzer = new IndraAnalyzer(request.getLanguage(),
                ModelMetadata.createTranslationVersion(vectorSpace.getMetadata()));

        List analyzedTerms = terms.stream().map(m -> new MutableTranslatedTerm(m, analyzer.analyze(m)))
                .collect(Collectors.toList());
        if (one != null) {
            analyzedTerms.add(new MutableTranslatedTerm(one, analyzer.analyze(one)));
        }

        translate((List<MutableTranslatedTerm>) analyzedTerms);
        return (List<AnalyzedTerm>) analyzedTerms;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs, VectorComposer termComposer, VectorComposer translationComposer) {
        return vectorSpace.getTranslatedVectorPairs((List<AnalyzedTranslatedPair>) analyzedPairs, termComposer, translationComposer);
    }
}
