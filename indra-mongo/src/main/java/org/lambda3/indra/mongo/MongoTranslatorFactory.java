package org.lambda3.indra.mongo;

import com.mongodb.MongoClient;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;

import java.util.HashSet;
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
public class MongoTranslatorFactory extends IndraTranslatorFactory<MongoIndraTranslator> {

    private static final String DEFAULT_DB_NAME_SUFFIX = "-Europarl_DGT_OpenSubtitile";
    private static final String DEFAULT_TARGET_LANG = "EN";

    private MongoClient mongoClient;
    private String dbNameSuffix;
    private Set<String> availableModels = new HashSet<>();

    public MongoTranslatorFactory(String mongoURI) {
        this(mongoURI, DEFAULT_DB_NAME_SUFFIX);
    }

    public MongoTranslatorFactory(String mongoURI, String dbNameSuffix) {
        mongoClient = new MongoClient(mongoURI);
        this.dbNameSuffix = dbNameSuffix;

        for (String s : mongoClient.listDatabaseNames()) {
            if (s.endsWith(dbNameSuffix)) {
                availableModels.add(s);
            }
        }
    }

    @Override
    protected MongoIndraTranslator doCreate(Params params) {
        return new MongoIndraTranslator(mongoClient, createDbName(params.language), DEFAULT_TARGET_LANG);
    }

    @Override
    protected Object createKey(Params params) {
        return createDbName(params.language);
    }

    private String createDbName(String fromLang) {
        return String.format("%s_%s-%s", fromLang.toLowerCase(), DEFAULT_TARGET_LANG.toLowerCase(), dbNameSuffix);
    }
}
