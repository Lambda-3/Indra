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
import org.lambda3.indra.core.CachedVectorSpace;
import org.lambda3.indra.core.composition.VectorComposer;

import java.util.*;

public class MockCachedVectorSpace extends CachedVectorSpace {

    public static final int VECTOR_SIZE = 5;
    public static final RealVector ZERO_VECTOR = new ArrayRealVector(VECTOR_SIZE);
    public static final RealVector ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, 1);
    public static final RealVector NEGATIVE_ONE_VECTOR = new ArrayRealVector(VECTOR_SIZE, -1);
    public static final RealVector TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, 2);
    public static final RealVector NEGATIVE_TWO_VECTOR = new ArrayRealVector(VECTOR_SIZE, -2);

    private Map<String, RealVector> vectors = new HashMap<>();

    public MockCachedVectorSpace(VectorComposer termComposer, VectorComposer translationComposer) {
        super(termComposer, translationComposer);
        vectors.put("love", new ArrayRealVector(new double[]{1, 0, 0, 0, 0}));
        vectors.put("plane", new ArrayRealVector(new double[]{0, 1, 0, 0, 0}));
        vectors.put("good", new ArrayRealVector(new double[]{0, 0, 1, 0, 0}));
        vectors.put("hot", new ArrayRealVector(new double[]{0, 0, 0, 1, 0}));
        vectors.put("south", new ArrayRealVector(new double[]{0, 0, 0, 0, 1}));
        vectors.put("hate", new ArrayRealVector(new double[]{-1, 0, 0, 0, 0}));
        vectors.put("car", new ArrayRealVector(new double[]{0, -1, 0, 0, 0}));
        vectors.put("bad", new ArrayRealVector(new double[]{0, 0, -1, 0, 0}));
        vectors.put("cold", new ArrayRealVector(new double[]{0, 0, 0, -1, 0}));
        vectors.put("north", new ArrayRealVector(new double[]{0, 0, 0, 0, -1}));

        vectors.put("mother", new ArrayRealVector(new double[]{3, 3, 0, 0, 0}));
        vectors.put("mom", new ArrayRealVector(new double[]{0, 0, 3, 0, 0}));
        vectors.put("matriarch", new ArrayRealVector(new double[]{0, 0, 0, 3, 3}));

        vectors.put("father", new ArrayRealVector(new double[]{-3, -3, 0, 0, 0}));
        vectors.put("dad", new ArrayRealVector(new double[]{0, 0, -3, 0, 0}));
        vectors.put("patriarch", new ArrayRealVector(new double[]{0, 0, 0, -3, -3}));

        vectors.put("machine", new ArrayRealVector(new double[]{2, 2, 0, 0, 0}));
        vectors.put("computer", new ArrayRealVector(new double[]{0, 0, 2, 2, 2}));

        vectors.put("test", new ArrayRealVector(new double[]{-2, -2, 0, 0, 0}));
        vectors.put("evaluation", new ArrayRealVector(new double[]{0, 0, -2, -2, -2}));

        //stemmed
        vectors.put("machin", vectors.get("machine"));
        vectors.put("comput", vectors.get("computer"));
        vectors.put("evalu", vectors.get("evaluation"));
    }

    @Override
    public boolean isSparse() {
        return false;
    }

    @Override
    public int getVectorSize() {
        return VECTOR_SIZE;
    }

    @Override
    protected void collectVectors(Collection<String> terms, int limit) {
        //does nothing
    }

    @Override
    protected List<RealVector> getFromCache(Collection<String> terms) {
        List<RealVector> termVectors = new ArrayList<>();
        terms.stream().
                filter(t -> this.vectors.containsKey(t)).
                forEach((t) -> termVectors.add(this.vectors.get(t)));
        return termVectors;
    }
}
