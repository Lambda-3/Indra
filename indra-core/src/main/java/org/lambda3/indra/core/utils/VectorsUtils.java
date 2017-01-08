package org.lambda3.indra.core.utils;


import java.util.*;

public class VectorsUtils {

    public static Map<Integer, Double> add(List<Map<Integer, Double>> vectors) {
        if (vectors == null) {
            throw new IllegalArgumentException("vectors can't be null");
        }

        Set<Integer> dimensions = new HashSet<>();
        vectors.forEach(d -> dimensions.addAll(d.keySet()));

        Map<Integer, Double> composed = new HashMap<>();

        dimensions.forEach(d -> {{
            double sum = vectors.parallelStream()
                    .filter(v -> v.containsKey(d))
                    .mapToDouble(v -> v.get(d)).sum();
            composed.put(d, sum);
        }});

        return composed;
    }

}
