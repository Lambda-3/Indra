package org.lambda3.indra.core.impl;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * ==========================License-End===============================
 */

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
