package org.lambda3.indra.core.filter;

import org.apache.commons.math3.linear.RealVector;

import java.util.LinkedHashMap;
import java.util.Map;

//https://stackoverflow.com/questions/4452939/in-java-how-to-find-if-first-character-in-a-string-is-upper-case-without-regex
//take a look in UTF-16 issues.
public class UppercaseFilter implements Filter {

    @Override
    public void filtrateVectors(Map<String, RealVector> vectors) {
        vectors.entrySet().removeIf(entry -> Character.isUpperCase(entry.getKey().charAt(0)));
    }

    @Override
    public void filtrateRelatedness(LinkedHashMap<String, Double> relatedness) {
        relatedness.entrySet().removeIf(entry -> Character.isUpperCase(entry.getKey().charAt(0)));
    }
}
