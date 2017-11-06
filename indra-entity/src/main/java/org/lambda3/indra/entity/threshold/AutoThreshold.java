package org.lambda3.indra.entity.threshold;

import com.google.auto.service.AutoService;
import org.lambda3.indra.entity.threshold.Threshold;

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