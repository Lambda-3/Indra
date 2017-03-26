package org.lambda3.indra.service.impl;

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

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.lambda3.indra.mongo.MongoVectorSpaceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

public final class Server {
    private static final String BASE_URI;
    private static final String protocol;
    private static final String host;
    private static final String port;
    private static final boolean mockMode;
    private static final String mongoURI;

    static {
        protocol = "http://";
        host = System.getProperty("indra.http.host", "localhost");
        port = System.getProperty("indra.http.port", "8916");
        BASE_URI = protocol + host + ":" + port;
        mockMode = Boolean.parseBoolean(System.getProperty("indra.mock", "false"));
        mongoURI = System.getProperty("indra.mongoURI", "mongodb://localhost:27017");
    }

    private Logger logger = LoggerFactory.getLogger(getClass());
    private HttpServer httpServer;

    public Server() {
        logger.info("Initializing Indra Service.");
        ResourceConfig rc = new ResourceConfig();
        rc.register(LoggingFilter.class);
        rc.register(JacksonFeature.class);
        rc.register(CatchAllExceptionMapper.class);
        rc.register(SerializationExceptionMapper.class);
        rc.register(ErrorMapper.class);

        if (mockMode) {
            logger.warn("MOCK mode.");
            rc.register(new MockedRelatednessResourceImpl());
        } else {
            MongoVectorSpaceFactory vectorSpaceFactory = new MongoVectorSpaceFactory(mongoURI);
            rc.register(new RelatednessResourceImpl(vectorSpaceFactory, null));
        }

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc, false);
    }

    public synchronized void start() throws IOException {
        httpServer.start();
        logger.info("Indra serving @ {}", BASE_URI);
    }

    public synchronized void stop() {
        logger.info("Terminating Indra Service.");
        httpServer.shutdownNow();
    }


}
