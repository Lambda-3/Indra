package org.lambda3.indra.pp;

import org.lambda3.indra.corpus.CorpusMetadata;

import java.util.Iterator;

public interface PreProcessor {

    Iterator<String> process(String text);

    CorpusMetadata getCorpusMetadata();
}
