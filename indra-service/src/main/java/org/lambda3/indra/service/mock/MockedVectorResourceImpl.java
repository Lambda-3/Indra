package org.lambda3.indra.service.mock;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
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
import org.apache.commons.math3.linear.RealVectorUtil;
import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
import org.lambda3.indra.response.SparseVectorResponse;
import org.lambda3.indra.request.VectorRequest;
import org.lambda3.indra.web.VectorResource;
import org.lambda3.indra.response.VectorResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * MockedVectorResourceImpl implementation that returns random vectors.
 * For testing puporses.
 */
public final class MockedVectorResourceImpl implements VectorResource {

    private static final int NUM_DIMENSIONS = 300;
    private static final UnitSphereRandomVectorGenerator rvg = new UnitSphereRandomVectorGenerator(NUM_DIMENSIONS);

    @Override
    public VectorResponse getVector(VectorRequest request) {
        Map<String, Map<Integer, Double>> terms = new HashMap<>();
        request.getTerms().forEach(t -> terms.put(t, RealVectorUtil.vectorToMap(new ArrayRealVector(rvg.nextVector()))));
        VectorResponse response = new SparseVectorResponse(request, terms);

        return response;
    }
}
