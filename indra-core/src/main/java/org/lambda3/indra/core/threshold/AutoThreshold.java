package org.lambda3.indra.core.threshold;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class AutoThreshold implements Threshold {

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {

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
            iter.next();
            count = 0;

            while (iter.hasNext()) {
                count++;
                iter.next();
                if (count >= pos) {
                    iter.remove();
                }
            }
        }
    }

    @Override
    public void configure(String... params) {
        //do nothing
    }
}