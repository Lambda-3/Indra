package org.lambda3.indra.text;

import java.util.Iterator;

public interface PreProcessor {

    Iterator<String> process(String text);

    CorpusMetadata getCorpusMetadata();
}
