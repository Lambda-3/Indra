package org.apache.commons.math3.linear;

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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * There is a bug in the class RealVector.Entry, which restricts its access. This is way this code was located
 * into this package.
 *
 * Bug track: https://issues.apache.org/jira/browse/MATH-1329
 *
 * As soon as it is solved, the method can be send to a class in the lambda3 package.
 */
public class RealVectorUtil {

    public static Map<Integer, Double> vectorToMap(RealVector vector) {
        Map<Integer, Double> mapVector = new HashMap<>();
        Iterator<RealVector.Entry> iter = vector.iterator();

        while (iter.hasNext()) {
            RealVector.Entry entry = iter.next();
            mapVector.put(entry.getIndex(), entry.getValue());
        }

        return mapVector;
    }

}
