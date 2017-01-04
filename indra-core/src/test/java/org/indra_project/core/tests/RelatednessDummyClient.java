package org.indra_project.core.tests;

import org.indra_project.core.*;
import org.indra_project.common.client.AnalyzedPair;
import org.indra_project.core.Params;
import org.indra_project.common.client.ScoredTextPair;

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
