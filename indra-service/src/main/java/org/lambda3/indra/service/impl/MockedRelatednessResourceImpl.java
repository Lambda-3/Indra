package org.lambda3.indra.service.impl;

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
