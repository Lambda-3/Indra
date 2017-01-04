package org.indra_project.rest.resources;

import org.restlet.resource.Post;

import java.io.Serializable;

public interface RelatednessResource extends Serializable {

    @Post("json")
    RelatednessResponse getRelatedness(RelatednessRequest request);
}
