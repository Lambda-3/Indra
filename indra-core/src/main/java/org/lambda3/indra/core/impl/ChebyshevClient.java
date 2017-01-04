package org.lambda3.indra.core.impl;


import org.lambda3.indra.core.Params;

public class ChebyshevClient extends RelatednessBaseClient {

    ChebyshevClient(Params params, MongoVectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        return 1500;
    }

    @Override
    protected double sim(double[] a, double[] b) {
        if (a.length != b.length)
            return 0;

        double max = 0;
        double tmp;

        for (int i = 0; i < a.length; ++i) {
            tmp = Math.abs((a[i] - b[i]));
            max = (tmp > max ? tmp : max);
        }

        double result = 1 / (1 + (max == Double.NaN ? 0 : max));
        return Math.abs(result);
    }
}
