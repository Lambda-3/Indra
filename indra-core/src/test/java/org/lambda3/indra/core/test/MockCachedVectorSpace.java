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
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;
import org.lambda3.indra.core.filter.Filter;
import org.lambda3.indra.core.vs.CachedVectorSpace;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockCachedVectorSpace extends CachedVectorSpace {

    public static final int VECTOR_SIZE = 5;
    public static final RealVector ZERO_VECTOR = new ArrayRealVector(VECTOR_SIZE);
    public static final RealVector ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, 1);
    public static final RealVector NEGATIVE_ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, -1);
    public static final RealVector TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, 2);
    public static final RealVector NEGATIVE_TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, -2);

    public MockCachedVectorSpace() {
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
            cache.put("machin", cache.get("machine"));
            cache.put("comput", cache.get("computer"));
            cache.put("evalu", cache.get("evaluation"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.metadata = loadMetadata();
    }

    public void cachePut(String term, RealVector vector) {
        cache.put(term, Optional.of(vector));
    }

    @Override
    public Map<String, Optional<RealVector>> loadAll(Iterable<? extends String> keys) throws Exception {
        Map<String, Optional<RealVector>> vectors = new HashMap<>();
        keys.forEach(k -> vectors.put(k, Optional.empty()));
        return vectors;
    }

    @Override
    protected ModelMetadata loadMetadata() {
        return ModelMetadata.createDefault().applyStemmer(0).removeAccents(false).dimensions(VECTOR_SIZE).sparse(false);
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk, Filter filter) {
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
