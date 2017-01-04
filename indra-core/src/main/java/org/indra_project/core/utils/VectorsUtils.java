package org.indra_project.core.utils;

import com.google.common.base.Preconditions;

import java.util.*;

public class VectorsUtils {

    public static Map<Integer, Double> add(List<Map<Integer, Double>> vectors) {
        Preconditions.checkNotNull(vectors);

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
