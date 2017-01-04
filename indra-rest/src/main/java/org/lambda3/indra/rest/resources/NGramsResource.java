package org.lambda3.indra.rest.resources;

import org.restlet.resource.Post;

import java.io.Serializable;

public interface NGramsResource extends Serializable {

    @Post("json")
    NGramsResponse getNGrams(NGramsRequest request);
}
