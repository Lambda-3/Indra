package org.lambda3.indra.client;

public abstract class VectorResponse extends AbstractBasicResponse {

    protected VectorResponse() {
        //jersey demands.
    }

    public VectorResponse(VectorRequest request) {
        super(request);
    }
}
