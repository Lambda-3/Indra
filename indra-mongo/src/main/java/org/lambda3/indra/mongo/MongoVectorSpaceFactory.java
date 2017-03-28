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
import com.mongodb.MongoClientURI;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.VectorSpaceFactory;
import org.lambda3.indra.core.composition.VectorComposer;
import org.lambda3.indra.core.exception.ModelNoFound;

import java.util.HashSet;
import java.util.Set;

public final class MongoVectorSpaceFactory extends VectorSpaceFactory<MongoVectorSpace> {

    private MongoClient mongoClient;
    private Set<String> availableModels;

    public MongoVectorSpaceFactory(String mongoURI) {
        if (mongoURI == null || mongoURI.isEmpty()) {
            throw new IllegalArgumentException("mongoURI can't be null nor empty");
        }
        this.mongoClient = new MongoClient(new MongoClientURI(mongoURI));

        availableModels = new HashSet<>();
        for (String s : mongoClient.listDatabaseNames()) {
            availableModels.add(s);
        }
    }

    @Override
    public MongoVectorSpace doCreate(Params params) throws ModelNoFound {
        if (availableModels.contains(getDBName(params))) {
            VectorComposer termComposer = this.vectorComposerFactory.getComposer(params.termComposition);
            VectorComposer translationComposer = this.vectorComposerFactory.getComposer(params.translationComposition);
            return new MongoVectorSpace(mongoClient, getDBName(params), termComposer, translationComposer);
        }

        throw new ModelNoFound(getDBName(params));
    }

    @Override
    public String createKey(Params params) {
        return getDBName(params);
    }

    private String getDBName(Params params) {
        return String.format("%s-%s-%s",
                params.model.toLowerCase(),
                params.language.toLowerCase(),
                params.corpusName.toLowerCase());
    }
}
