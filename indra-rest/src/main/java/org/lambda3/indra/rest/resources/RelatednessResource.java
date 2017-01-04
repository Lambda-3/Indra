package org.lambda3.indra.rest.resources;

import org.restlet.resource.Post;

import java.io.Serializable;

public interface RelatednessResource extends Serializable {

    @Post("json")
    RelatednessResponse getRelatedness(RelatednessRequest request);
}
