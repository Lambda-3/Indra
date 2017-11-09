package org.lambda3.indra.entity;

import org.lambda3.indra.entity.Extensible;

import java.util.LinkedHashMap;

public interface Threshold extends Extensible {

    LinkedHashMap<String, Double> apply(LinkedHashMap<String, Double> relatedness);
}
