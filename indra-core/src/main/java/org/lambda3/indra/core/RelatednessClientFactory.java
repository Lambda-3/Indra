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

import org.lambda3.indra.client.RelatednessRequest;
import org.lambda3.indra.core.translation.TranslatorFactory;
import org.lambda3.indra.core.vs.VectorSpace;
import org.lambda3.indra.core.vs.VectorSpaceFactory;

import java.util.Objects;

public final class RelatednessClientFactory extends IndraCachedFactory<RelatednessClient, RelatednessRequest> {
    private VectorSpaceFactory vectorSpaceFactory;
    private TranslatorFactory translatorFactory;

    public RelatednessClientFactory(VectorSpaceFactory vectorSpaceFactory, TranslatorFactory translatorFactory) {
        this.vectorSpaceFactory = Objects.requireNonNull(vectorSpaceFactory);
        this.translatorFactory = Objects.requireNonNull(translatorFactory);
    }

    @Override
    protected RelatednessClient doCreate(RelatednessRequest request) {
        VectorSpace vectorSpace = vectorSpaceFactory.create(request);

        if (request.isMt()) {
            if (translatorFactory == null) {
                throw new IllegalStateException("Translation-based relatedness not activated.");
            }

            return new TranslationBasedRelatednessClient(translatorFactory.create(request), request, vectorSpace);
        } else {
            return new StandardRelatednessClient(request, vectorSpace);
        }
    }

    @Override
    protected String createKey(RelatednessRequest request) {
        //todo threshold is stateless should not be into the relatedness client.
        return request.getCorpus() + request.getLanguage() + request.getModel() + request.isMt() + request.getThreshold();
    }
}
