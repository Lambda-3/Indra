package org.lambda3.indra.core.filter;

import org.apache.commons.math3.linear.RealVector;

import java.util.LinkedHashMap;
import java.util.Map;

public interface Filter {

    void filtrateVectors(Map<String, RealVector> vectors);

    void filtrateRelatedness(LinkedHashMap<String, Double> relatedness);
}
