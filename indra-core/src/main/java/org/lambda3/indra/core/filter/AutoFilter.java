package org.lambda3.indra.core.filter;

import org.apache.commons.math3.linear.RealVector;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

//TODO call them threshold. and the uppercase filter. better.
public class AutoFilter implements Filter {

    @Override
    public void filtrateVectors(Map<String, RealVector> vectors) {
        throw new RuntimeException(String.format("%s does not implement filtrateVectors",
                this.getClass().getSimpleName()));
    }

    @Override
    public void filtrateRelatedness(LinkedHashMap<String, Double> relatedness) {

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
}