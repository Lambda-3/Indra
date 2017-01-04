package org.indra_project.core.impl;

import org.indra_project.core.Params;

public class CityBlockClient extends RelatednessBaseClient {

    CityBlockClient(Params params, MongoVectorSpace vectorSpace) {
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

        double sum = 0.0;

        for (int i = 0; i < a.length; ++i) {
            sum += Math.abs((a[i] - b[i]));
        }

        double result = 1 / (1 + (sum == Double.NaN ? 0 : sum));
        return Math.abs(result);
    }
}
