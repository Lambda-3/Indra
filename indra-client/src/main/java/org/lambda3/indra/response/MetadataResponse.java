package org.lambda3.indra.response;


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
