package org.lambda3.indra.service.mapper;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

import org.lambda3.indra.client.IndraBadRequestException;
import org.lambda3.indra.core.exception.IndraException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;

@Provider
@Singleton
public final class CatchAllExceptionMapper implements ExceptionMapper<Throwable> {
    private static Logger logger = LoggerFactory.getLogger(CatchAllExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        if (exception instanceof IndraException) {
            logger.error("Oops!", exception);
            return Response.status(Response.Status.BAD_REQUEST).entity(new HashMap<String, String>() {{
                put("msg", exception.getLocalizedMessage());
            }}).build();
        }

        if (exception instanceof WebApplicationException) {
            logger.error("Web App Error!", exception);
            return ((WebApplicationException) exception).getResponse();
        }

        if (exception instanceof IndraBadRequestException) {
            logger.error("Bad request!", exception);
            return Response.status(Response.Status.BAD_REQUEST).entity(new HashMap<String, String>() {{
                put("msg", exception.getMessage());
            }}).build();
        }

        logger.error("Internal error!", exception);
        return Response.status(500).entity(new HashMap<String, String>() {{
            put("msg", exception.getLocalizedMessage());
        }}).build();
    }
}
