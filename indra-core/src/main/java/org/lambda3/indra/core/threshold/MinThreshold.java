package org.lambda3.indra.core.threshold;

import org.lambda3.indra.Threshold;

import java.util.LinkedHashMap;

public class MinThreshold implements Threshold {
    private float min;

    public MinThreshold(float min) {
        this.min = min;
    }

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> (entry.getValue() < min));
    }

    @Override
    public String getName() {
        return "min";
    }
}
