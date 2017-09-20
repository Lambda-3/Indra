package org.lambda3.indra;

import java.util.LinkedHashMap;

public interface Threshold extends Extensible {

    LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness);
}
