package org.lambda3.indra.core.test;

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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.AnalyzedTerm;
import org.lambda3.indra.core.vs.AbstractVectorSpace;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.lambda3.indra.filter.Filter;
import org.lambda3.indra.model.ModelMetadata;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MockCachedVectorSpace extends AbstractVectorSpace {

    private static final int VECTOR_SIZE = 5;
    static final RealVector ZERO_VECTOR = new ArrayRealVector(VECTOR_SIZE);
    static final RealVector ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, 1);
    static final RealVector NEGATIVE_ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, -1);
    static final RealVector TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, 2);
    static final RealVector NEGATIVE_TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, -2);

    private Map<String, RealVector> localCache = new HashMap<>();

    MockCachedVectorSpace() {
        cachePut("throne", new ArrayRealVector(new double[]{5, 6, 7, 8, 9}));
        cachePut("love", new ArrayRealVector(new double[]{1, 0, 0, 0, 0}));
        cachePut("plane", new ArrayRealVector(new double[]{0, 1, 0, 0, 0}));
        cachePut("good", new ArrayRealVector(new double[]{0, 0, 1, 0, 0}));
        cachePut("hot", new ArrayRealVector(new double[]{0, 0, 0, 1, 0}));
        cachePut("south", new ArrayRealVector(new double[]{0, 0, 0, 0, 1}));
        cachePut("hate", new ArrayRealVector(new double[]{-1, 0, 0, 0, 0}));
        cachePut("car", new ArrayRealVector(new double[]{0, -1, 0, 0, 0}));
        cachePut("bad", new ArrayRealVector(new double[]{0, 0, -1, 0, 0}));
        cachePut("cold", new ArrayRealVector(new double[]{0, 0, 0, -1, 0}));
        cachePut("north", new ArrayRealVector(new double[]{0, 0, 0, 0, -1}));

        cachePut("mother", new ArrayRealVector(new double[]{3, 3, 0, 0, 0}));
        cachePut("mom", new ArrayRealVector(new double[]{0, 0, 3, 0, 0}));
        cachePut("matriarch", new ArrayRealVector(new double[]{0, 0, 0, 3, 3}));

        cachePut("father", new ArrayRealVector(new double[]{-3, -3, 0, 0, 0}));
        cachePut("dad", new ArrayRealVector(new double[]{0, 0, -3, 0, 0}));
        cachePut("patriarch", new ArrayRealVector(new double[]{0, 0, 0, -3, -3}));

        cachePut("machine", new ArrayRealVector(new double[]{2, 2, 0, 0, 0}));
        cachePut("computer", new ArrayRealVector(new double[]{0, 0, 2, 2, 2}));

        cachePut("test", new ArrayRealVector(new double[]{-2, -2, 0, 0, 0}));
        cachePut("evaluation", new ArrayRealVector(new double[]{0, 0, -2, -2, -2}));

        //stemmed
        try {
            localCache.put("machin", localCache.get("machine"));
            localCache.put("comput", localCache.get("computer"));
            localCache.put("evalu", localCache.get("evaluation"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.metadata = loadMetadata();
    }

    private void cachePut(String term, RealVector vector) {
        localCache.put(term, vector);
    }

    @Override
    protected ModelMetadata loadMetadata() {
        CorpusMetadata cm = CorpusMetadataBuilder.newCorpusMetadata("bla", "en").
                applyStemmer(0).removeAccents(false).build();
        return new ModelMetadata("bla bla", false, VECTOR_SIZE, -1, -1, cm);
    }

    @Override
    protected Map<String, RealVector> collectVectors(Iterable<? extends String> terms) {
        Map<String, RealVector> results = new HashMap<>();
        for (String term : terms) {
            if (localCache.keySet().contains(term)) {
                results.put(term, localCache.get(term));
            }
        }

        return results;
    }

    @Override
    public Map<String, RealVector> getNearestVectors(AnalyzedTerm term, int topk, Filter filter) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public Collection<String> getNearestTerms(AnalyzedTerm term, int topk, Filter filter) {
        throw new UnsupportedOperationException("not implemented yet.");
    }

    @Override
    public void close() throws IOException {
        //do nothing.
    }
}
