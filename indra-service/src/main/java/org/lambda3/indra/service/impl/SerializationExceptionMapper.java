package org.lambda3.indra.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Singleton
public class SerializationExceptionMapper implements ExceptionMapper<JsonProcessingException> {
    private static Logger logger = LoggerFactory.getLogger(SerializationExceptionMapper.class);

    @Override
    public Response toResponse(JsonProcessingException exception) {
        logger.error("Serialization Error", exception);
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
