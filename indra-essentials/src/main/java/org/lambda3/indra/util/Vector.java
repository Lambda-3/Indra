package org.lambda3.indra.util;

import org.apache.commons.math3.linear.RealVector;

public abstract class Vector {

    public final String term;
    public final RealVector content;
    protected int dimensions;

    public Vector(int dimensions, String... parts) {
        this.dimensions = dimensions;
        this.term = parts[0];
        content = digestContent(parts[1]);
    }

    public abstract RealVector digestContent(String content);
}
