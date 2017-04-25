package org.lambda3.indra.mongo;

/*-
 * ==========================License-Start=============================
 * Indra Mongo Module
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

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.lambda3.indra.client.MutableTranslatedTerm;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public final class MongoIndraTranslator extends IndraTranslator {

    private static final String LEX_COLLECTION = "lex";
    private static final String TERM_FIELD = "term";
    private static final String TRANSLATION_FIELD = "tr";
    private static final String NULL_VALUE = "NULL";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private MongoClient mongoClient;
    private String dbName;


    MongoIndraTranslator(MongoClient mongoClient, String dbName) {
        if (mongoClient == null) {
            throw new IllegalArgumentException("mongoClient can't be null");
        }

        this.mongoClient = mongoClient;
        this.dbName = dbName;
    }

    private MongoCollection<Document> getLexCollection() {
        return mongoClient.getDatabase(dbName).getCollection(LEX_COLLECTION);
    }

    @Override
    public void translate(List<MutableTranslatedTerm> terms) {

        Set<String> allTokens = new HashSet<>();
        terms.forEach(t -> allTokens.addAll(t.getAnalyzedTokens()));
        logger.debug("Translating {} MutableAnalyzedTerms, resulting in {} tokens", terms.size(), allTokens.size());

        Map<String, List<String>> translations = doTranslate(allTokens);
        for (MutableTranslatedTerm term : terms) {

            for (String token : term.getAnalyzedTokens()) {
                List<String> tokenTranslations = translations.get(token);
                if (tokenTranslations != null) {
                    term.putTranslatedTokens(token, tokenTranslations);
                }
            }
        }
    }

    private Map<String, List<String>> doTranslate(Set<String> tokens) {
        MongoCollection<Document> lexColl = getLexCollection();
        FindIterable<Document> lexs = lexColl.find(Filters.in(TERM_FIELD, tokens));

        Map<String, List<String>> res = new HashMap<>();
        for (Document doc : lexs) {
            Document tr = (Document) doc.get(TRANSLATION_FIELD);

            if (tr != null) {
                tr.remove(NULL_VALUE);
                res.put(doc.getString(TERM_FIELD), getRelevantTranslations((Map) tr));
            }
        }

        return res;
    }

}
