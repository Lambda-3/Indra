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
import org.lambda3.indra.client.AbstractBasicRequest;
import org.lambda3.indra.core.exception.ModelNoFound;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.TranslatorFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class MongoTranslatorFactory extends TranslatorFactory {

    private static final String DEFAULT_DB_NAME_SUFFIX = "Europarl_DGT_OpenSubtitile";

    private MongoClient mongoClient;
    private String dbNameSuffix;

    public MongoTranslatorFactory(String mongoURI) {
        this(new MongoClient(mongoURI), DEFAULT_DB_NAME_SUFFIX);
    }

    public MongoTranslatorFactory(MongoClient client) {
        this(client, DEFAULT_DB_NAME_SUFFIX);
    }

    private MongoTranslatorFactory(MongoClient client, String dbNameSuffix) {
        this.mongoClient = Objects.requireNonNull(client);
        this.dbNameSuffix = Objects.requireNonNull(dbNameSuffix);
    }

    @Override
    protected MongoIndraTranslator doCreate(AbstractBasicRequest<?> request) throws ModelNoFound {
        String dbName = getDbName(request.getLanguage(), IndraTranslator.DEFAULT_TRANSLATION_TARGET_LANGUAGE);
        if (getAvailableModels().contains(dbName)) {
            return new MongoIndraTranslator(mongoClient, dbName);
        }

        throw new ModelNoFound(dbName);
    }

    @Override
    protected String createKey(AbstractBasicRequest<?> request) {
        return getDbName(request.getLanguage(), IndraTranslator.DEFAULT_TRANSLATION_TARGET_LANGUAGE);
    }

    private String getDbName(String fromLang, String toLang) {
        return String.format("%s_%s-%s", fromLang.toLowerCase(), toLang.toLowerCase(), dbNameSuffix);
    }

    @Override
    public Collection<String> getAvailableModels() {
        Set<String> availableModels = new HashSet<>();
        for (String s : mongoClient.listDatabaseNames()) {
            if (s.endsWith(dbNameSuffix)) {
                availableModels.add(s);
            }
        }
        return availableModels;
    }

    @Override
    public void close() throws IOException {
        this.mongoClient.close();
    }
}
