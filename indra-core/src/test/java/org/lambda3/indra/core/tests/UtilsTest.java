package org.lambda3.indra.core.tests;

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

import org.lambda3.indra.core.utils.VectorsUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsTest {

    @Test
    public void addVectorsTest() {
        Map<Integer, Double> v1 = new HashMap<Integer, Double>() {{
            put(1, 0.1);
            put(2, 0.1);
            put(3, 0.1);
            put(6, 0.1);
            put(10, 0.1);
        }};

        Map<Integer, Double> v2 = new HashMap<Integer, Double>() {{
            put(1, 0.1);
            put(2, 0.1);
            put(3, 0.1);
            put(6, 0.1);
            put(12, 0.1);
        }};

        Map<Integer, Double> expected = new HashMap<Integer, Double>() {{
            put(1, 0.2);
            put(2, 0.2);
            put(3, 0.2);
            put(6, 0.2);
            put(10, 0.1);
            put(12, 0.1);
        }};

        List<Map<Integer, Double>> vectors = new ArrayList<Map<Integer, Double>>() {{
            add(v1);
            add(v2);
        }};

        Assert.assertEquals(expected, VectorsUtils.add(vectors));
    }

}
