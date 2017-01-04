package org.lambda3.indra.core.impl;

import org.lambda3.indra.core.Params;

public class DiceClient extends RelatednessBaseClient {

    DiceClient(Params params, MongoVectorSpace vectorSpace) {
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

        double min = 0.0;
        double sum = 0.0;

        for (int i = 0; i < a.length; ++i) {
            if (a[i] > b[i]) {
                min += b[i];
            } else {
                min += a[i];
            }
            sum += a[i] + b[i];
        }

        if (sum == 0)
            return 0;

        double result = 2 * min / sum;
        return Math.abs(result);
    }
}
