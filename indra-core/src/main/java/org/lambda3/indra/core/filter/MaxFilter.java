package org.lambda3.indra.core.filter;

import org.apache.commons.math3.linear.RealVector;

import java.util.LinkedHashMap;
import java.util.Map;

public class MaxFilter implements Filter {
    private float max;

    public MaxFilter(float max) {
        this.max = max;
    }

    @Override
    public void filtrateVectors(Map<String, RealVector> vectors) {
        throw new RuntimeException(String.format("%s does not implement filtrateVectors",
                this.getClass().getSimpleName()));
    }

    @Override
    public void filtrateRelatedness(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> (entry.getValue() <= max));
    }
}
