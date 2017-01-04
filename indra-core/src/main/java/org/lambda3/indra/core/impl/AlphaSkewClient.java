package org.lambda3.indra.core.impl;

import org.lambda3.indra.core.Params;

public class AlphaSkewClient extends RelatednessBaseClient {

    AlphaSkewClient(Params params, MongoVectorSpace vectorSpace) {
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

        double alpha = 0.99;
        double divergence = 0.0;

        for (int i = 0; i < a.length; ++i) {
            if (a[i] > 0.0 && b[i] > 0.0) {
                divergence += a[i] * Math.log(a[i] / ((1 - alpha) * a[i] + alpha * b[i]));
            }
        }

        double result = (1 - (divergence / Math.sqrt(2 * Math.log(2))));
        return Math.abs(result);
    }
}
