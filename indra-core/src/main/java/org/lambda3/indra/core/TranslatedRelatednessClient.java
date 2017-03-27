package org.lambda3.indra.core;

import org.lambda3.indra.client.*;
import org.lambda3.indra.core.translation.Translator;

import java.util.*;

public abstract class TranslatedRelatednessClient extends RelatednessClient {
    private Translator translator;

    public TranslatedRelatednessClient(Params params, VectorSpace vectorSpace, Translator translator) {
        super(params, vectorSpace);
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
            IndraAnalyzer newLangAnalyzer = new IndraAnalyzer(translator.targetLang);

            for (MutableTranslatedTerm term : analyzedTerms) {
                Map<String, List<String>> transTokens = term.getTranslatedTokens();
                for (String token : transTokens.keySet()) {
                    term.putAnalyzedTranslatedTokens(token, newLangAnalyzer.stem(transTokens.get(token)));
                }
            }

        } else {
            logger.error("Translator is not available, but getParams().translate is true.");
        }

        return analyzedPairs;
    }

    @Override
    protected Map<? extends AnalyzedPair, VectorPair> getVectors(List<? extends AnalyzedPair> analyzedPairs) {
        return vectorSpace.getTranslatedVectorPairs((List<AnalyzedTranslatedPair>) analyzedPairs);
    }
}
