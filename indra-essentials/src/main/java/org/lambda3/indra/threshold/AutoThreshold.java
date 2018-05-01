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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@AutoService(Threshold.class)
public class AutoThreshold implements Threshold {

    @Override
    public LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness) {

        if (relatedness.size() > 2) {

            double max = Double.MIN_VALUE;
            int pos = relatedness.size();
            Iterator<Map.Entry<String, Double>> iter = relatedness.entrySet().iterator();
            Map.Entry<String, Double> previous = iter.next();
            int count = 0;

            while (previous.getValue() > 0.9 && iter.hasNext()) {
                previous = iter.next();
                count++;
            }

            while (iter.hasNext()) {
                Map.Entry<String, Double> curr = iter.next();
                count++;

                double diff = previous.getValue() - curr.getValue();
                if (diff > max) {
                    max = diff;
                    pos = count;
                }

                previous = curr;
            }

            iter = relatedness.entrySet().iterator();
            count = 0;
            LinkedHashMap<String, Double> result = new LinkedHashMap<>();
            while (iter.hasNext() && count < pos) {
                count++;
                Map.Entry<String, Double> entry = iter.next();
                result.put(entry.getKey(), entry.getValue());
            }

            return result;
        }

        return relatedness;
    }

    @Override
    public String getName() {
        return "auto";
    }
}
