package org.lambda3.indra.threshold;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
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

import com.google.auto.service.AutoService;
import org.lambda3.indra.threshold.Threshold;

import java.util.LinkedHashMap;

@AutoService(Threshold.class)
public class MaxThreshold implements Threshold {
    private float max;

    public MaxThreshold(float max) {
        this.max = max;
    }

    @Override
    public LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness) {
        LinkedHashMap<String, Double> results = new LinkedHashMap<>();
        relatedness.entrySet().stream().filter(entry -> (entry.getValue() <= max)).
                forEach(e -> results.put(e.getKey(), e.getValue()));
        return results;
    }

    @Override
    public String getName() {
        return "max";
    }
}
