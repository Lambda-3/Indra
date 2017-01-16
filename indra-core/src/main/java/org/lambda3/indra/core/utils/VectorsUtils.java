package org.lambda3.indra.core.utils;

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


import java.util.*;

public class VectorsUtils {

    public static Map<Integer, Double> add(List<Map<Integer, Double>> vectors) {
        if (vectors == null) {
            throw new IllegalArgumentException("vectors can't be null");
        }

        Set<Integer> dimensions = new HashSet<>();
        vectors.forEach(d -> dimensions.addAll(d.keySet()));

        Map<Integer, Double> composed = new HashMap<>();

        dimensions.forEach(d -> {{
            double sum = vectors.parallelStream()
                    .filter(v -> v.containsKey(d))
                    .mapToDouble(v -> v.get(d)).sum();
            composed.put(d, sum);
        }});

        return composed;
    }

}
