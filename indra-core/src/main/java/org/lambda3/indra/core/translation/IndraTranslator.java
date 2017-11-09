package org.lambda3.indra.core.translation;

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
import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.corpus.CorpusMetadata;

import java.io.Closeable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class IndraTranslator implements Closeable {

    public static final String DEFAULT_DB_NAME_SUFFIX = "Europarl_DGT_OpenSubtitile";
    public static final String DEFAULT_TRANSLATION_TARGET_LANGUAGE = "EN";

    private CorpusMetadata corpusMetadata;
    private IndraAnalyzer analyzer;

    /**
     * Translate each AnalyzedTerm token by token and store into MutableTranslatedTerm.translatedTokens.
     *
     * @param terms
     */
    public abstract void translate(List<MutableTranslatedTerm> terms);

    public abstract CorpusMetadata loadCorpusMetadata();

    public IndraAnalyzer getAnalyzer() {
        if (analyzer == null) {
            analyzer = new IndraAnalyzer(getCorpusMetadata());
        }
        return analyzer;
    }

    public CorpusMetadata getCorpusMetadata() {
        if (corpusMetadata == null) {
            corpusMetadata = loadCorpusMetadata();
        }
        return corpusMetadata;
    }

    public static List<String> getRelevantTranslations(Map<String, Double> tr) {
        List<String> res = new LinkedList<>();

        if (tr.size() <= 2) {
            res.addAll(tr.keySet());
        } else {

            LinkedHashMap<String, Double> tempWords = new LinkedHashMap<>();
            tr.keySet().forEach(k -> tempWords.put(k, tr.get(k)));

            LinkedHashMap<String, Double> sortedWords = sortByValue(tempWords);

            double maxDiff = Double.MIN_VALUE * -1;
            Double lastScore = sortedWords.entrySet().iterator().next().getValue();

            for (String word : sortedWords.keySet()) {
                Double score = sortedWords.get(word);
                double diff = lastScore - score;
                if (diff > maxDiff) {
                    maxDiff = diff;
                }

                lastScore = score;
            }

            lastScore = sortedWords.entrySet().iterator().next().getValue();
            for (String word : sortedWords.keySet()) {
                Double score = sortedWords.get(word);
                double diff = lastScore - score;
                if (diff >= maxDiff) {
                    break;
                }

                res.add(word);
                lastScore = score;
            }
        }

        return res;
    }

    private static LinkedHashMap<String, Double> sortByValue(Map<String, Double> map) {
        return map.entrySet().stream().sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
