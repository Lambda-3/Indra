package org.lambda3.indra.response;

import org.lambda3.indra.request.VectorRequest;

public abstract class VectorResponse extends AbstractBasicResponse {

    protected VectorResponse() {
        //jersey demands.
    }

    public VectorResponse(VectorRequest request) {
        super(request);
    }
}
