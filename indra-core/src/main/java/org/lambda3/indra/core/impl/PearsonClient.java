package org.lambda3.indra.core.impl;

import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessBaseClient;
import org.lambda3.indra.core.VectorSpace;

public class PearsonClient extends RelatednessBaseClient {

    private PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();

    PearsonClient(Params params, VectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected double sim(RealVector r1, RealVector r2, boolean sparse) {
        return pearsonsCorrelation.correlation(r1.toArray(), r2.toArray());
    }
}
