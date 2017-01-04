package org.lambda3.indra.core.impl;

import org.lambda3.indra.core.Params;

public class Jaccard2Client extends RelatednessBaseClient {

    Jaccard2Client(Params params, MongoVectorSpace vectorSpace) {
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
        double max = 0.0;

        for (int i = 0; i < a.length; ++i) {
            if (a[i] > b[i]) {
                min += b[i];
                max += a[i];
            } else {
                min += a[i];
                max += b[i];
            }
        }

        if (max == 0)
            return 0;

        return Math.abs(min / max);
    }
}
