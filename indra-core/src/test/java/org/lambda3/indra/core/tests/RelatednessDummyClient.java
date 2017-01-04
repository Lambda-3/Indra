package org.lambda3.indra.core.tests;

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
