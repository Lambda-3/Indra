package org.lambda3.indra.core.tests;

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

import org.lambda3.indra.core.*;
import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.common.client.ScoredTextPair;

import java.util.*;
import java.util.stream.Collectors;

public class RelatednessDummyClient extends RelatednessClient {

    private static Random rnd = new Random();
    private final Params params;

    public RelatednessDummyClient(Params params) {
        this.params = params;
    }

    @Override
    protected List<ScoredTextPair> compute(List<AnalyzedPair> pairs) {
        return pairs.stream().
                map(p -> new ScoredTextPair(p, rnd.nextDouble())).
                collect(Collectors.toList());
    }

    @Override
    protected Params getParams() {
        return params;
    }

}
