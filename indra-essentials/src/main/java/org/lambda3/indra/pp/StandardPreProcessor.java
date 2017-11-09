package org.lambda3.indra.pp;

import org.lambda3.indra.corpus.CorpusMetadata;

import java.util.Iterator;
import java.util.Objects;

public class StandardPreProcessor implements PreProcessor {
    private final CorpusMetadata metadata;

    public StandardPreProcessor(CorpusMetadata metadata) {
        this.metadata = Objects.requireNonNull(metadata);
    }

    @Override
    public Iterator<String> process(String text) {
        if (text == null) {
            return null;
        } else {
            return new StandardPreProcessorIterator(metadata, text);
        }
    }

    @Override
    public CorpusMetadata getCorpusMetadata() {
        return metadata;
    }
}
