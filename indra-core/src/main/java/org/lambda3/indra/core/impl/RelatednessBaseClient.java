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

import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.ScoredTextPair;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.core.VectorSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class RelatednessBaseClient extends RelatednessClient {
    private VectorSpace vectorSpace;
    private Params params;

    RelatednessBaseClient(Params params, MongoVectorSpace vectorSpace) {
        if (params == null || vectorSpace == null) {
            throw new IllegalArgumentException("Missing required arguments.");
        }
        this.vectorSpace = vectorSpace;
        this.params = params;

    }

    protected abstract int getVectorSizeLimit();

    protected abstract double sim(double[] v1, double[] v2);

    @Override
    protected Params getParams() {
        return params;
    }

    @Override
    protected List<ScoredTextPair> compute(List<AnalyzedPair> pairs) {
        Map<AnalyzedPair, VectorPair> vectorPairs = vectorSpace.getVectors(pairs, getVectorSizeLimit());

        List<ScoredTextPair> scoredTextPairs = new ArrayList<>();

        vectorPairs.entrySet().stream().forEach(e -> {
            AnalyzedPair pair = e.getKey();
            VectorPair vectorPair = e.getValue();

            if (vectorPair.v1 != null && vectorPair.v2 != null) {
                double[] v1 = vectorPair.v1.values().stream().mapToDouble(d -> d).toArray();
                double[] v2 = vectorPair.v2.values().stream().mapToDouble(d -> d).toArray();
                scoredTextPairs.add(new ScoredTextPair(pair, sim(v1, v2)));
            }
            else {
                scoredTextPairs.add(new ScoredTextPair(pair, 0));
            }

        });

        return scoredTextPairs;
    }
}
