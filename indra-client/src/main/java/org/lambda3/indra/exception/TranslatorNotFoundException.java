package org.lambda3.indra.exception;

public class TranslatorNotFoundException extends IndraRuntimeException {

    public TranslatorNotFoundException(String lang) {
        super("No translator found for language '" + lang + "'.");
    }
}
