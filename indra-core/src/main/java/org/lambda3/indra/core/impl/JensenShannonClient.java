package org.lambda3.indra.core.impl;

import org.lambda3.indra.core.Params;

public class JensenShannonClient extends RelatednessBaseClient {

    JensenShannonClient(Params params, MongoVectorSpace vectorSpace) {
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

        double divergence = 0.0;
        double avr = 0.0;

        for (int i = 0; i < a.length; ++i) {
            avr = (a[i] + b[i]) / 2;

            if (a[i] > 0.0 && avr > 0.0) {
                divergence += a[i] * Math.log(a[i] / avr);
            }
        }
        for (int i = 0; i < b.length; ++i) {
            avr = (a[i] + b[i]) / 2;

            if (b[i] > 0.0 && avr > 0.0) {
                divergence += a[i] * Math.log(b[i] / avr);
            }
        }

        double result = 1 - (divergence / (2 * Math.sqrt(2 * Math.log(2))));
        return Math.abs(result);
    }
}
