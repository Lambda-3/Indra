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
