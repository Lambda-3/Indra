package org.indra_project.rest.resources;

import org.restlet.resource.Post;

import java.io.Serializable;

public interface NGramsResource extends Serializable {

    @Post("json")
    NGramsResponse getNGrams(NGramsRequest request);
}
