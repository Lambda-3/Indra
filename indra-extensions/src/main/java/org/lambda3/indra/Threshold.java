package org.lambda3.indra;

import java.util.LinkedHashMap;

public interface Threshold extends Extensible {

    void apply(LinkedHashMap<String, Double> relatedness);
}
