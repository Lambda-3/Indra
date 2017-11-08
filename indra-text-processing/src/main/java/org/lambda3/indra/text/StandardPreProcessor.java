package org.lambda3.indra.text;

import java.util.Iterator;
import java.util.Objects;

public class StandardPreProcessor implements PreProcessor {
    private final CorpusMetadata metadata;

    public StandardPreProcessor(CorpusMetadata metadata) {
        this.metadata = Objects.requireNonNull(metadata);
    }

    @Override
    public Iterator<String> process(String text) {
        return new StandardPreProcessorIterator(metadata, text);
    }

    @Override
    public CorpusMetadata getCorpusMetadata() {
        return metadata;
    }
}
