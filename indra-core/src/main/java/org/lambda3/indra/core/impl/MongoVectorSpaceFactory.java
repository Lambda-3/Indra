package org.lambda3.indra.core.impl;

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

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.VectorSpaceFactory;
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
    public MongoVectorSpace create(Params params) throws ModelNoFound {
        if (availableModels.contains(params.getDBName())) {
            return new MongoVectorSpace(mongoClient, params.getDBName());
        }
        throw new ModelNoFound(params.getDBName());
    }
}
