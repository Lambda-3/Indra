package org.lambda3.indra.threshold;

import com.google.auto.service.AutoService;
import org.lambda3.indra.threshold.Threshold;

import java.util.LinkedHashMap;

@AutoService(Threshold.class)
public class MaxThreshold implements Threshold {
    private float max;

    public MaxThreshold(float max) {
        this.max = max;
    }

    @Override
    public LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness) {
        LinkedHashMap<String, Double> results = new LinkedHashMap<>();
        relatedness.entrySet().stream().filter(entry -> (entry.getValue() <= max)).
                forEach(e -> results.put(e.getKey(), e.getValue()));
        return results;
    }

    @Override
    public String getName() {
        return "max";
    }
}
