package org.lambda3.indra.util;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;

public class DenseVector extends Vector {

    public DenseVector(String content) {
        super(-1, content.split("\t"));
    }

    @Override
    public RealVector digestContent(String content) {
        String[] parts = content.split(" ");
        double[] vector = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            vector[i] = Double.parseDouble(parts[i]);
        }

        return new ArrayRealVector(vector, false);
    }
}
