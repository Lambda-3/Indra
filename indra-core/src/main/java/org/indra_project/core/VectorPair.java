package org.indra_project.core;

import java.util.Map;

public final class VectorPair {
    public Map<Integer, Double> v1;
    public Map<Integer, Double> v2;

    public VectorPair() { }

    public VectorPair(Map<Integer, Double> v1, Map<Integer, Double> v2) {
        this.v1 = v1;
        this.v2 = v2;
    }
}
