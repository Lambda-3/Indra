package org.lambda3.indra.core.threshold;

import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.core.filter.Filter;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxThreshold implements Threshold {
    float max;

    MaxThreshold() {

    }

    public MaxThreshold(float max) {
        this.max = max;
    }

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> (entry.getValue() <= max));
    }

    @Override
    public void configure(String... params) {
        max = Integer.parseInt(params[0]);
    }
}
