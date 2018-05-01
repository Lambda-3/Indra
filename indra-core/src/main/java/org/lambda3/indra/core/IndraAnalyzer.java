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

import org.lambda3.indra.*;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.pp.PreProcessor;
import org.lambda3.indra.pp.StandardPreProcessor;
import org.lambda3.indra.pp.StandardPreProcessorIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.SnowballProgram;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class IndraAnalyzer {

    private static Logger logger = LoggerFactory.getLogger(IndraAnalyzer.class);

    private CorpusMetadata metadata;
    private PreProcessor preprocessor;


    public IndraAnalyzer(CorpusMetadata metadata) {
        this.metadata = metadata;
        this.preprocessor = new StandardPreProcessor(metadata);
    }

    @SuppressWarnings("unchecked")
    public <T extends AnalyzedPair> T analyze(TextPair pair, Class<T> clazz) {
        if (clazz.equals(AnalyzedTranslatedPair.class)) {
            AnalyzedTranslatedPair analyzedPair = new AnalyzedTranslatedPair(pair);
            analyzedPair.setTranslatedTerm1(new MutableTranslatedTerm(pair.t1, analyze(pair.t1)));
            analyzedPair.setTranslatedTerm2(new MutableTranslatedTerm(pair.t2, analyze(pair.t2)));
            return (T) analyzedPair;
        }

        AnalyzedPair analyzedPair = new AnalyzedPair(pair);
        analyzedPair.setAnalyzedTerm1(new AnalyzedTerm(pair.t1, analyze(pair.t1)));
        analyzedPair.setAnalyzedTerm2(new AnalyzedTerm(pair.t2, analyze(pair.t2)));

        return (T) analyzedPair;
    }

    public List<String> analyze(String text) {
        List<String> tokens = new LinkedList<>();
        preprocessor.process(text).forEachRemaining(tokens::add);

        return tokens;
    }


    public List<String> stem(Collection<String> tokens) {
        List<String> stemmed = new LinkedList<>();

        SnowballProgram stemmer = StandardPreProcessorIterator.getStemmer(metadata.language);
        for (String token : tokens) {
            String stemmedToken = token;

            for (int i = 0; i < metadata.applyStemmer; i++) {
                stemmer.setCurrent(stemmedToken);
                stemmer.stem();
                stemmedToken = stemmer.getCurrent();
            }

            stemmed.add(stemmedToken);
        }

        return stemmed;
    }
}