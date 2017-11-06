package org.lambda3.indra.entity.threshold;

import org.lambda3.indra.entity.threshold.Threshold;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopThreshold implements Threshold {

    private final int top;

    public TopThreshold(int top) {
        this.top = top;
    }

    @Override
    public LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness) {

        int limit = top;

        if (relatedness.size() <= top) {
            return relatedness;
        } else {
            LinkedHashMap<String, Double> newOne = new LinkedHashMap<>();
            Iterator<Map.Entry<String, Double>> iter = relatedness.entrySet().iterator();
            while (iter.hasNext() && limit-- > 0) {
                Map.Entry<String, Double> item = iter.next();
                newOne.put(item.getKey(), item.getValue());
            }

            return newOne;
        }
    }
}
