package org.lambda3.indra.client;

import javax.ws.rs.WebApplicationException;

/**
 * Flags a Client's fault.
 */
public final class ClientError extends WebApplicationException {

    public ClientError(String message) {
        super(message);
    }

    public ClientError(String message, Throwable cause) {
        super(message, cause);
    }
}
