package org.lambda3.indra.service.impl;

import org.lambda3.indra.client.InfoResource;
import org.lambda3.indra.client.ResourceResponse;

import java.util.Collections;

public final class MockedInfoResourceImpl extends InfoResource {

    @Override
    public ResourceResponse getResources() {
        return new ResourceResponse(Collections.emptyList(), Collections.emptyList());
    }
}
