package org.lambda3.indra.text;

import java.util.HashMap;
import java.util.Map;

public class ModelMetadata implements Metadata {

    public static final String MODEL_NAME = "modelName";
    public static final String SPARSE = "sparse";
    public static final String PARAMS = "params";
    public static final String CORPUS_METADATA = "corpusMetadata";

    public final String modelName;
    public final boolean sparse;
    public final Map<String, Object> params;
    public final CorpusMetadata corpusMetadata;

    public ModelMetadata(String modelName, boolean sparse, CorpusMetadata corpusMetadata, Map<String, Object> params) {
        this.modelName = modelName;
        this.sparse = sparse;
        this.corpusMetadata = corpusMetadata;
        this.params = params;
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(MODEL_NAME, this.modelName);
        map.put(SPARSE, this.sparse);
        map.put(PARAMS, this.params);
        map.put(CORPUS_METADATA, corpusMetadata.asMap());

        return map;
    }
}
