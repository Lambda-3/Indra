package org.lambda3.indra.service;

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

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.core.annoy.AnnoyVectorSpaceFactory;
import org.lambda3.indra.core.lucene.LuceneTranslatorFactory;
import org.lambda3.indra.core.lucene.LuceneVectorSpaceFactory;
import org.lambda3.indra.core.translation.TranslatorFactory;
import org.lambda3.indra.core.vs.HubVectorSpaceFactory;
import org.lambda3.indra.mongo.MongoTranslatorFactory;
import org.lambda3.indra.mongo.MongoVectorSpaceFactory;
import org.lambda3.indra.response.VersionResponse;
import org.lambda3.indra.service.impl.InfoResourceImpl;
import org.lambda3.indra.service.impl.NeighborsResourceImpl;
import org.lambda3.indra.service.impl.RelatednessResourceImpl;
import org.lambda3.indra.service.impl.VectorResourceImpl;
import org.lambda3.indra.service.mapper.CatchAllExceptionMapper;
import org.lambda3.indra.service.mapper.SerializationExceptionMapper;
import org.lambda3.indra.service.mock.MockedInfoResourceImpl;
import org.lambda3.indra.service.mock.MockedNeighborsResourceImpl;
import org.lambda3.indra.service.mock.MockedRelatednessResourceImpl;
import org.lambda3.indra.service.mock.MockedVectorResourceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public final class Server {
    private static final String PROTOCOL = "http";

    private final String baseUri;

    private Logger logger = LoggerFactory.getLogger(getClass());
    private HttpServer httpServer;
    private HubVectorSpaceFactory spaceFactory = new HubVectorSpaceFactory();
    private TranslatorFactory translatorFactory;

    Server() {
        String host = System.getProperty("indra.http.host", "0.0.0.0");
        String port = System.getProperty("indra.http.port", "8916");
        this.baseUri = String.format("%s://%s:%s", PROTOCOL, host, port);
        logger.info("Initializing Indra Service v. {}.", new VersionResponse().getVersion());
        ResourceConfig rc = new ResourceConfig();
        rc.register(LoggingFeature.class);
        rc.register(JacksonJsonProvider.class);
        rc.register(CatchAllExceptionMapper.class);
        rc.register(SerializationExceptionMapper.class);

        boolean mockMode = Boolean.parseBoolean(System.getProperty("indra.mock", "false"));

        if (mockMode) {
            logger.warn("MOCK mode.");
            rc.register(new MockedRelatednessResourceImpl());
            rc.register(new MockedVectorResourceImpl());
            rc.register(new MockedInfoResourceImpl());
            rc.register(new MockedNeighborsResourceImpl());

        } else {
            String mongoURI = System.getProperty("indra.mongoURI");
            String annoyBaseDir = System.getProperty("indra.annoyBaseDir");
            String luceneVectorsBaseDir = System.getProperty("indra.luceneVectorsBaseDir");
            String luceneTranslationBaseDir = System.getProperty("indra.luceneTranslationBaseDir");

            if (annoyBaseDir != null) {
                logger.info("Initializing AnnoyVectorSpaceFactory from {}.", annoyBaseDir);
                spaceFactory.addFactory(new AnnoyVectorSpaceFactory(new File(annoyBaseDir)));
            } else {
                logger.warn("No AnnoyVectorSpaceFactory.");
            }

            if (luceneVectorsBaseDir != null) {
                logger.info("Initializing LuceneVectorSpaceFactory from {}.", luceneVectorsBaseDir);
                spaceFactory.addFactory(new LuceneVectorSpaceFactory(new File(luceneVectorsBaseDir)));
            } else if (mongoURI != null) {
                logger.info("Initializing LuceneVectorSpaceFactory from {}.", mongoURI);
                logger.warn("mongo support is limited and deprecated. Please migrate to the Lucene indexes.");
                spaceFactory.addFactory(new MongoVectorSpaceFactory(mongoURI));
            }

            if (luceneTranslationBaseDir != null) {
                logger.info("Initializing LuceneTranslatorFactory from {}.", luceneTranslationBaseDir);
                translatorFactory = new LuceneTranslatorFactory(new File(luceneTranslationBaseDir));
            } else if (mongoURI != null) {
                logger.info("Initializing MongoTranslatorFactory from {}.", mongoURI);
                logger.warn("mongo support is limited and deprecated. Please migrate to the Lucene indexes.");
                translatorFactory = new MongoTranslatorFactory(mongoURI);
            }

            IndraDriver driver = new IndraDriver(spaceFactory, translatorFactory);

            rc.register(new RelatednessResourceImpl(driver));
            rc.register(new VectorResourceImpl(driver));
            rc.register(new NeighborsResourceImpl(driver));
            rc.register(new InfoResourceImpl(spaceFactory, translatorFactory));
        }

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), rc, false);
    }

    public synchronized void start() throws IOException {
        httpServer.start();
        logger.info("Indra serving @ {}", baseUri);
    }

    public synchronized void stop() {
        logger.info("Terminating Indra Service.");
        httpServer.shutdownNow();
        try {
            if (spaceFactory != null) {
                spaceFactory.close();
            }
        } catch (IOException e) {
            logger.error("error closing the vector space factory.");
        } finally {
            try {
                if (translatorFactory != null) {
                    translatorFactory.close();
                }
            } catch (IOException e) {
                logger.error("error closing the translator factory.");
            }
        }
    }
}
