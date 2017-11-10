package org.lambda3.indra.util;

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;

public class SparseVector extends Vector {

    public SparseVector(int dimensions, String content) {
        super(dimensions, content.split("\t"));
    }

    @Override
    public RealVector digestContent(String content) {
        RealVector vector = new OpenMapRealVector(dimensions);

        String[] parts = content.split(" ");
        for (String part : parts) {
            int sep = part.lastIndexOf(":");
            vector.addToEntry(Integer.parseInt(part.substring(0, sep)), Double.parseDouble(part.substring(sep + 1)));
        }

        return vector;
    }
}
