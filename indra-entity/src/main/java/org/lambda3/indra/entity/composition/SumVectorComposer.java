package org.lambda3.indra.entity.composition;

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
import org.lambda3.indra.entity.composition.VectorComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SumVectorComposer implements VectorComposer {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public RealVector compose(List<RealVector> vectors) {
        logger.trace("Composing {} vectors", vectors.size());

        if (vectors.isEmpty()) {
            return null;
        } else if (vectors.size() == 1) {
            return vectors.get(0);
        } else {
            RealVector sum = vectors.get(0).add(vectors.get(1));
            for (int i = 2; i < vectors.size(); i++) {
                sum = sum.add(vectors.get(i));
            }
            return sum;
        }
    }
}
