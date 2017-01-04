package org.indra_project.core.impl;

import com.google.common.base.Preconditions;
import org.indra_project.core.*;
import org.indra_project.common.client.AnalyzedPair;
import org.indra_project.core.Params;
import org.indra_project.common.client.ScoredTextPair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//TODO: JavaDoc
abstract class RelatednessBaseClient extends RelatednessClient {
    private VectorSpace vectorSpace;
    private Params params;

    RelatednessBaseClient(Params params, MongoVectorSpace vectorSpace) {
        Preconditions.checkNotNull(vectorSpace);
        Preconditions.checkNotNull(params);
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
