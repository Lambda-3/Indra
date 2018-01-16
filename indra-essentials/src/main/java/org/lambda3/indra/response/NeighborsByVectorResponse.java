package org.lambda3.indra.response;

import org.lambda3.indra.request.NeighborsByVectorRequest;

import java.util.Collection;

public class NeighborsByVectorResponse extends AbstractBasicResponse {

    private Collection<String> terms;
    private int topk;

    protected NeighborsByVectorResponse() {
        //jersey demands.
    }

    public NeighborsByVectorResponse(NeighborsByVectorRequest request, Collection<String> terms) {
        super(request);
        this.terms = terms;
        this.topk = request.getTopk();
    }

    public int getTopk() {
        return topk;
    }

    public Collection<String> getTerms() {
        return terms;
    }

    @Override
    public String toString() {
        return "NeighborsByVectorResponse{" +
                "topk=" + topk +
                ", terms size=" + terms.size() +
                '}';
    }
}
