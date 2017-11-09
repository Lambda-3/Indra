package org.lambda3.indra.service.impl;

import org.lambda3.indra.exception.InvalidRequestException;
import org.lambda3.indra.request.AbstractBasicRequest;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.HashMap;

public class RequestValidator {

    public static void validate(AbstractBasicRequest<?> request) {
        try {
            request.validate();
        } catch (InvalidRequestException e) {
            throw new BadRequestException(Response.status(400).entity(new HashMap<String, String>() {{
                put("msg", "This request contains one or more errors:\\n" + e.getMessage());
            }}).build());
        }
    }
}
