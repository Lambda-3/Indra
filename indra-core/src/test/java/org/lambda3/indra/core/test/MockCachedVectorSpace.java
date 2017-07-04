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
import org.lambda3.indra.core.CachedVectorSpace;
import org.lambda3.indra.core.composition.VectorComposer;

import java.util.Collection;
import java.util.Map;

public class MockCachedVectorSpace extends CachedVectorSpace {

    public static final int VECTOR_SIZE = 5;
    public static final RealVector ZERO_VECTOR = new ArrayRealVector(VECTOR_SIZE);
    public static final RealVector ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, 1);
    public static final RealVector NEGATIVE_ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, -1);
    public static final RealVector TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, 2);
    public static final RealVector NEGATIVE_TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, -2);

    public MockCachedVectorSpace(VectorComposer termComposer, VectorComposer translationComposer) {
        super(termComposer, translationComposer);
        vectorsCache.put("throne", new ArrayRealVector(new double[]{5, 6, 7, 8, 9}));
        vectorsCache.put("love", new ArrayRealVector(new double[]{1, 0, 0, 0, 0}));
        vectorsCache.put("plane", new ArrayRealVector(new double[]{0, 1, 0, 0, 0}));
        vectorsCache.put("good", new ArrayRealVector(new double[]{0, 0, 1, 0, 0}));
        vectorsCache.put("hot", new ArrayRealVector(new double[]{0, 0, 0, 1, 0}));
        vectorsCache.put("south", new ArrayRealVector(new double[]{0, 0, 0, 0, 1}));
        vectorsCache.put("hate", new ArrayRealVector(new double[]{-1, 0, 0, 0, 0}));
        vectorsCache.put("car", new ArrayRealVector(new double[]{0, -1, 0, 0, 0}));
        vectorsCache.put("bad", new ArrayRealVector(new double[]{0, 0, -1, 0, 0}));
        vectorsCache.put("cold", new ArrayRealVector(new double[]{0, 0, 0, -1, 0}));
        vectorsCache.put("north", new ArrayRealVector(new double[]{0, 0, 0, 0, -1}));

        vectorsCache.put("mother", new ArrayRealVector(new double[]{3, 3, 0, 0, 0}));
        vectorsCache.put("mom", new ArrayRealVector(new double[]{0, 0, 3, 0, 0}));
        vectorsCache.put("matriarch", new ArrayRealVector(new double[]{0, 0, 0, 3, 3}));

        vectorsCache.put("father", new ArrayRealVector(new double[]{-3, -3, 0, 0, 0}));
        vectorsCache.put("dad", new ArrayRealVector(new double[]{0, 0, -3, 0, 0}));
        vectorsCache.put("patriarch", new ArrayRealVector(new double[]{0, 0, 0, -3, -3}));

        vectorsCache.put("machine", new ArrayRealVector(new double[]{2, 2, 0, 0, 0}));
        vectorsCache.put("computer", new ArrayRealVector(new double[]{0, 0, 2, 2, 2}));

        vectorsCache.put("test", new ArrayRealVector(new double[]{-2, -2, 0, 0, 0}));
        vectorsCache.put("evaluation", new ArrayRealVector(new double[]{0, 0, -2, -2, -2}));

        //stemmed
        vectorsCache.put("machin", vectorsCache.get("machine"));
        vectorsCache.put("comput", vectorsCache.get("computer"));
        vectorsCache.put("evalu", vectorsCache.get("evaluation"));
        this.metadata = loadMetadata();
    }

    @Override
    protected void collectVectors(Collection<String> terms, int limit) {
        //does nothing
    }

    @Override
    protected ModelMetadata loadMetadata() {
        return ModelMetadata.createDefault().applyStemmer(0).removeAccents(false).dimensions(VECTOR_SIZE).sparse(false);
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk) {
        throw new UnsupportedOperationException("not implemented yet.");
    }
}
