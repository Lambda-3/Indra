package org.lambda3.indra.core.function;

import org.apache.commons.math3.linear.RealVector;

public class AbsoluteCosineRelatednessFunction implements RelatednessFunction {

    @Override
    public double sim(RealVector r1, RealVector r2, boolean sparse) {
        return Math.abs(r1.cosine(r2));
    }
}
