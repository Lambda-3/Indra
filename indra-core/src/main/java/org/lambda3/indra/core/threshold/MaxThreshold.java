package org.lambda3.indra.core.threshold;

import com.google.auto.service.AutoService;
import org.lambda3.indra.Threshold;

import java.util.LinkedHashMap;

@AutoService(Threshold.class)
public class MaxThreshold implements Threshold {
    private float max;

    public MaxThreshold() {

    }

    public MaxThreshold(float max) {
        this.max = max;
    }

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> (entry.getValue() > max));
    }

    @Override
    public String getName() {
        return "max";
    }
}
