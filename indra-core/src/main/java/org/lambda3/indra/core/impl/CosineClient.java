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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.core.Params;

public class CosineClient extends RelatednessBaseClient {

    CosineClient(Params params, MongoVectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        //TODO: Should be configurable?
        // Attention: May break the cache vectors if comes from client requests
        // Pay attention to the same behavior on the other clients.
        return 1500;
    }

    @Override
    protected double sim(double[] v1, double[] v2) {
        RealVector rv1 = new ArrayRealVector(v1);
        RealVector rv2 = new ArrayRealVector(v2);
        return rv1.cosine(rv2);
    }
}
