package org.lambda3.indra.service.impl;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
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
import org.lambda3.indra.rest.resources.RelatednessRequest;
import org.lambda3.indra.rest.resources.RelatednessResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * RelatednessResource implementation that randomly assigns a relatedness value.
 * For testing puporses.
 */
public class MockedRelatednessResourceImpl extends RelatednessServerResource {

    private static Random rnd = new Random();

    @Override
    public RelatednessResponse getRelatedness(RelatednessRequest request) {
        RelatednessResponse response = new RelatednessResponse();
        response.corpus = request.corpus;
        response.language = request.language;
        response.model = request.model;
        response.scoreFunction = request.scoreFunction;

        Collection<ScoredTextPair> scored = new ArrayList<>();
        request.pairs.forEach(p -> {
            AnalyzedPair analyzedPair = new AnalyzedPair(p);
            ScoredTextPair stp = new ScoredTextPair(analyzedPair, rnd.nextDouble());
            scored.add(stp);
        });

        response.pairs = scored;
        return response;
    }
}
