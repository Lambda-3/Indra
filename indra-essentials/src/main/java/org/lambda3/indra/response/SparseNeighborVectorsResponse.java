package org.lambda3.indra.response;

import org.lambda3.indra.request.NeighborsVectorsRequest;

import java.util.Map;
import java.util.Objects;

public class SparseNeighborVectorsResponse extends NeighborVectorsResponse {

    private Map<String, Map<String, Map<Integer, Double>>> terms;

    protected SparseNeighborVectorsResponse() {
        //jersey demands.
    }

    public SparseNeighborVectorsResponse(NeighborsVectorsRequest request, Map<String, Map<String, Map<Integer, Double>>> terms) {
        super(request);
        this.terms = Objects.requireNonNull(terms);
    }

    public Map<String, Map<String, Map<Integer, Double>>> getTerms() {
        return terms;
    }

    @Override
    public String toString() {


        return "DenseNeighborVectorsResponse{" +
                "terms=" + terms +
                '}';
    }
}
