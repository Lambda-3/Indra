package org.lambda3.indra.model;

import org.lambda3.indra.Metadata;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelMetadata implements Metadata {

    public static final String MODEL = "model";
    public static final String SPARSE = "sparse";
    public static final String DIMENSIONS = "dimensions";
    public static final String VOCAB_SIZE = "vocabSize";
    public static final String WINDOW_SIZE = "windowSize";
    public static final String MIN_WORD_FREQUENCY = "minWordFrequency";
    public static final String CORPUS_METADATA = "corpusMetadata";

    public final String modelName;
    public final boolean sparse;
    public final long dimensions;
    public final long vocabSize;
    public final long windowSize;
    public final long minWordFrequency;
    public final CorpusMetadata corpusMetadata;

    public ModelMetadata(Map<String, Object> map) {
        this.modelName = (String) map.get(MODEL);
        this.sparse = (boolean) map.get(SPARSE);
        this.dimensions = (long) map.get(DIMENSIONS);
        this.vocabSize = (long) map.get(VOCAB_SIZE);
        this.windowSize = (long) map.get(WINDOW_SIZE);
        this.minWordFrequency = (long) map.get(MIN_WORD_FREQUENCY);
        Map<String, Object> corpusMetaDataMap = (Map<String, Object>) map.get(CORPUS_METADATA);
        this.corpusMetadata = CorpusMetadataBuilder.fromMap(corpusMetaDataMap);
    }

    public ModelMetadata(String modelName, boolean sparse, long dimensions, long vocabSize, long windowSize, long minWordFrequency,
                         CorpusMetadata corpusMetadata) {
        this.modelName = modelName;
        this.sparse = sparse;
        this.dimensions = dimensions;
        this.vocabSize = vocabSize;
        this.windowSize = windowSize;
        this.minWordFrequency = minWordFrequency;
        this.corpusMetadata = corpusMetadata;
    }

    @Override
    public Map<String, Object> asMap() {
        Map<String, Object> map = new HashMap<>();
        map.put(MODEL, this.modelName);
        map.put(SPARSE, this.sparse);
        map.put(DIMENSIONS, this.dimensions);
        map.put(VOCAB_SIZE, this.vocabSize);
        map.put(WINDOW_SIZE, this.windowSize);
        map.put(MIN_WORD_FREQUENCY, this.minWordFrequency);
        map.put(CORPUS_METADATA, corpusMetadata.asMap());

        return map;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModelMetadata that = (ModelMetadata) o;
        return sparse == that.sparse &&
                dimensions == that.dimensions &&
                vocabSize == that.vocabSize &&
                windowSize == that.windowSize &&
                minWordFrequency == that.minWordFrequency &&
                Objects.equals(modelName, that.modelName) &&
                Objects.equals(corpusMetadata, that.corpusMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, sparse, dimensions, vocabSize, windowSize, minWordFrequency, corpusMetadata);
    }

    public String getConciseName() {
        return String.format("%s-%s-%s", modelName, corpusMetadata.language,
                corpusMetadata.corpusName).toLowerCase();
    }
}
