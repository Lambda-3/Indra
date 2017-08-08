package org.lambda3.indra.core.threshold;

import org.lambda3.indra.core.Servable;

import java.util.LinkedHashMap;

public interface Threshold extends Servable {

    void apply(LinkedHashMap<String, Double> relatedness);
}
