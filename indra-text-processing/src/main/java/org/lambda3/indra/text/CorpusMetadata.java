package org.lambda3.indra.text;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CorpusMetadata implements Metadata {

    public final String corpusName;
    public final String language;
    public final String description;
    public final String encoding;
    public final long applyStemmer;
    public final boolean removeAccents;
    public final boolean applyLowercase;
    public final boolean replaceNumbers;
    public final long minTokenLength;
    public final long maxTokenLength;
    public final Set<String> stopWords;
    public final Map<String, Collection<String>> transformers;

    private Map<String, Object> data;

    CorpusMetadata(Map<String, Object> data) {
        this.data = data;
        corpusName = (String) data.get(CorpusMetadataBuilder.CORPUS_NAME);
        language = (String) data.get(CorpusMetadataBuilder.LANGUAGE);
        description = (String) data.get(CorpusMetadataBuilder.DESCRIPTION);
        encoding = (String) data.get(CorpusMetadataBuilder.ENCODING);
        applyStemmer = (long) data.get(CorpusMetadataBuilder.APPLY_STEMMER);
        removeAccents = (boolean) data.get(CorpusMetadataBuilder.REMOVE_ACCENTS);
        applyLowercase = (boolean) data.get(CorpusMetadataBuilder.APPLY_LOWERCASE);
        replaceNumbers = (boolean) data.get(CorpusMetadataBuilder.REPLACE_NUMBERS);
        minTokenLength = (long) data.get(CorpusMetadataBuilder.MIN_TOKEN_LENGTH);
        maxTokenLength = (long) data.get(CorpusMetadataBuilder.MAX_TOKEN_LENGTH);
        stopWords = new HashSet<>((Collection<String>) data.get(CorpusMetadataBuilder.STOP_WORDS));
        transformers = (Map<String, Collection<String>>) data.get(CorpusMetadataBuilder.TRANSFORMERS);

        if (corpusName == null || language == null) {
            throw new IllegalArgumentException("Neither 'corpusName' nor 'language' can be null.");
        }
    }

    @Override
    public Map<String, Object> asMap() {
        return data;
    }
}
