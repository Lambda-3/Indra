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

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.core.codecs.BinaryCodecs;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public final class BinaryCodecsTest {

    @Test
    public void denseSerializationTest() throws IOException {
        double[] v1 = new double[]{1.52, 1 / 3, -3.1};
        byte[] b = BinaryCodecs.marshall(v1);
        RealVector realVector = BinaryCodecs.unmarshall(b, false, 3);
        Assert.assertEquals(v1, realVector.toArray());
    }

    @Test
    public void sparseSerializationTest() throws IOException {
        Random random = new Random(31);

        Map<Integer, Double> v1 = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            v1.put(Math.abs(random.nextInt()), random.nextDouble());
        }

        int dim = Collections.max(v1.keySet()) + 1;

        byte[] b = BinaryCodecs.marshall(v1);
        RealVector realVector = BinaryCodecs.unmarshall(b, true, dim);

        Assert.assertEquals(dim, realVector.getDimension());

        Map<Integer, Double> v2 = RealVectorUtil.vectorToMap(realVector);
        Assert.assertEquals(v1.size(), v2.size());

        for (int i : v1.keySet()) {
            Assert.assertEquals(v1.get(i), v2.get(i));
        }
    }
}
