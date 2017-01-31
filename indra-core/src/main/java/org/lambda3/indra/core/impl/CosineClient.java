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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.VectorSpace;

public class CosineClient extends RelatednessBaseClient {

    CosineClient(Params params, VectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        //TODO: Should be configurable?
        // Attention: May break the cache vectors if comes from client requests
        // Pay attention to the same behavior on the other clients.
        return 1500;
    }

    @Override
    protected double sim(double[] v1, double[] v2) {
        RealVector rv1 = new ArrayRealVector(v1);
        RealVector rv2 = new ArrayRealVector(v2);
        return rv1.cosine(rv2);
    }
}
