package org.lambda3.indra.client;

import java.util.Map;

public final class DenseVectorResponse extends VectorResponse {
    private Map<String, double[]> terms;

    private DenseVectorResponse() {
        //jersey demands.
    }

    public DenseVectorResponse(VectorRequest request, Map<String, double[]> terms) {
        super(request);
        this.terms = terms;
    }

    public Map<String, double[]> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return "DenseVectorResponse{" + super.toString() +
                "terms=" + terms +
                '}';
    }
}
