package org.lambda3.indra.service.impl;

import org.lambda3.indra.core.exception.IndraError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
@Singleton
public final class CatchAllExceptionMapper implements ExceptionMapper<Exception> {
    private static Logger logger = LoggerFactory.getLogger(CatchAllExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        logger.error("Oops!", exception);

        if (exception instanceof IndraError) {
            return Response.status(Response.Status.BAD_REQUEST).entity(new HashMap<String, String>() {{
                put("msg", exception.getLocalizedMessage());
            }}).build();
        }

        return Response.status(500).build();
    }
}
