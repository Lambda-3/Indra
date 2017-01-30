package org.lambda3.indra.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public final class ErrorMapper implements ExceptionMapper<WebApplicationException> {
    private static Logger logger = LoggerFactory.getLogger(ErrorMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        logger.error("Web App Error!", exception);
        return exception.getResponse();
    }
}
