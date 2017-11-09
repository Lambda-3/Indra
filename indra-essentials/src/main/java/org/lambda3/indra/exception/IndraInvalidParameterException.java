package org.lambda3.indra.exception;

public class IndraInvalidParameterException extends IndraException {

    public IndraInvalidParameterException(String message, Throwable e) {
        super(message, e);
    }

    public IndraInvalidParameterException(String message) {
        super(message);
    }
}
