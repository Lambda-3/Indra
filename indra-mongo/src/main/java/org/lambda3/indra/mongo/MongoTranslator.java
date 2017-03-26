package org.lambda3.indra.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.lambda3.indra.client.MutableAnalyzedTerm;
import org.lambda3.indra.core.translation.Translator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class MongoTranslator implements Translator {

    private static final String LEX_COLLECTION = "lex";
    private static final String TERM_FIELD = "term";
    private static final String TRANSLATION_FIELD = "tr";

    private static final String DB_NAME_SUFFIX = "";

    private MongoClient mongoClient;
    private Set<String> availableModels = new HashSet<>();

    public MongoTranslator(String mongoURI) {
        if (mongoURI == null || mongoURI.isEmpty()) {
            throw new IllegalArgumentException("mongoURI can't be null nor empty");
        }
        this.mongoClient = new MongoClient(new MongoClientURI(mongoURI));
        for (String s : mongoClient.listDatabaseNames()) {
            availableModels.add(s);
        }
    }

    @Override
    public void translate(List<MutableAnalyzedTerm> terms) {

    }
}
