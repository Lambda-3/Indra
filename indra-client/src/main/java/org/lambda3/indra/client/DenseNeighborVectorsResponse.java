package org.lambda3.indra.client;

import java.util.Map;
import java.util.Objects;

public class DenseNeighborVectorsResponse extends NeighborVectorsResponse {
    private Map<String, Map<String, double[]>> terms;

    protected DenseNeighborVectorsResponse() {
        //jersey demands.
    }

    public DenseNeighborVectorsResponse(NeighborsVectorsRequest request, Map<String, Map<String, double[]>> terms) {
        super(request);
        this.terms = Objects.requireNonNull(terms);
    }

    public Map<String, Map<String, double[]>> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return "DenseNeighborVectorsResponse{" +
                "terms=" + terms +
                '}';
    }
}
