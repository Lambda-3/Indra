package org.lambda3.indra.core.vs;

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

import org.lambda3.indra.client.AbstractBasicRequest;
import org.lambda3.indra.core.exception.ModelNotFoundException;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class HubVectorSpaceFactory extends VectorSpaceFactory {
    private List<VectorSpaceFactory> factories = new LinkedList<>();

    public void addFactory(VectorSpaceFactory factory) {
        this.factories.add(factory);
    }

    @Override
    protected VectorSpace doCreate(AbstractBasicRequest request) {
        ModelNotFoundException last = null;
        for (VectorSpaceFactory factory : factories) {
            try {
                return factory.create(request);
            } catch (ModelNotFoundException e) {
                last = e;
            }
        }
        throw last;
    }

    @Override
    protected String createKey(AbstractBasicRequest request) {
        return String.format("%s-%s-%s-%b", request.getModel(),
                request.getLanguage(), request.getCorpus(), request.isMt());
    }

    @Override
    public Collection<String> getAvailableModels() {
        return factories.stream().map(VectorSpaceFactory::getAvailableModels)
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    @Override
    public void close() throws IOException {
        for (VectorSpaceFactory factory : factories) {
            factory.close();
        }
    }
}
