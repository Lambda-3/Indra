package org.lambda3.indra.client;


import java.util.Map;

public final class MetadataResponse {
    private Map<String, Object> metadata;

    public MetadataResponse(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }
}
