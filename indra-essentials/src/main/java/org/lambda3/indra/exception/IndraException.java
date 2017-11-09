package org.lambda3.indra.exception;

public class IndraException extends Exception {

    public IndraException(String message, Throwable e) {
        super(message, e);
    }

    public IndraException(String message) {
        super(message);
    }
}
