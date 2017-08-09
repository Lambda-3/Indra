package org.lambda3.indra.core.threshold;

import java.util.LinkedHashMap;

public class MinThreshold implements Threshold {
    float min;

    MinThreshold() {

    }

    public MinThreshold(float min) {
        this.min = min;
    }

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> (entry.getValue() >= min));
    }

    @Override
    public void configure(String... params) {
        min = Float.parseFloat(params[0]);
    }
}
