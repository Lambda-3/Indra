package org.lambda3.indra.client;

import java.util.Map;

public final class SparseVectorResponse extends VectorResponse {

    private Map<String, Map<Integer, Double>> terms;

    private SparseVectorResponse() {
        //jersey demands.
    }

    public SparseVectorResponse(VectorRequest request, Map<String, Map<Integer, Double>> terms) {
        super(request);
        this.terms = terms;
    }

    public Map<String, Map<Integer, Double>> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return "SparseVectorResponse{" + super.toString() +
                "terms=" + terms +
                '}';
    }
}
