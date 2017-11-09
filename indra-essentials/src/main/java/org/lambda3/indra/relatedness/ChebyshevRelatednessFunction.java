package org.lambda3.indra.relatedness;

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


import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.relatedness.RelatednessFunction;

public class ChebyshevRelatednessFunction implements RelatednessFunction {

    @Override
    public double sim(RealVector r1, RealVector r2, boolean sparse) {
        if (r1.getDimension() != r2.getDimension()) {
            return 0;
        }

        double max = 0;
        double tmp;

        for (int i = 0; i < r1.getDimension(); ++i) {
            tmp = Math.abs((r1.getEntry(i) - r2.getEntry(i)));
            max = (tmp > max ? tmp : max);
        }

        double result = 1 / (1 + (max == Double.NaN ? 0 : max));
        return Math.abs(result);
    }
}
