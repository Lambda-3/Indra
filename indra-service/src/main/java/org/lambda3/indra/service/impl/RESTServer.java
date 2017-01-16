package org.lambda3.indra.service.impl;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * ==========================License-End===============================
 */

import org.lambda3.indra.service.config.Configuration;
import org.lambda3.indra.service.config.RunOptions;
import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Protocol;
import org.restlet.routing.Router;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RESTServer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Configuration configuration;
    private final Component component;
    private final RunOptions runOptions;

    public RESTServer(RunOptions options, Configuration config) {
        logger.info("Configuration: {}", config);

        if (config.security == null) {
            //TODO: Remove this kind of stuff and validate using json-schema!
            throw new RuntimeException("Missing security section in the config file.");
        }

        this.runOptions = options;
        this.configuration = config;
        this.component = new Component();
        this.component.getServers().add(Protocol.HTTP, options.httpPort);

        final Restlet restlet = new Application() {
            @Override
            public Restlet createInboundRoot() {
                Context ctx = getContext();
                ctx.getAttributes().put(Configuration.class.getCanonicalName(), configuration);
                Router router = new Router(ctx);
                configureRoutes(router);
                return router;
            }
        };

        if (!configuration.security.enabled) {
            logger.warn("Security is disabled! Accepting requests from any client");
            this.component.getDefaultHost().attach(restlet);
        }
        else {
            ChallengeAuthenticator guardedApp =
                    new ChallengeAuthenticator(new Context(), ChallengeScheme.HTTP_BASIC, "Indra Realm");
            MapVerifier verifier = new MapVerifier();
            verifier.getLocalSecrets().put(configuration.security.user, configuration.security.pass.toCharArray());
            guardedApp.setVerifier(verifier);
            guardedApp.setNext(restlet);
            this.component.getDefaultHost().attach(guardedApp);
        }
    }

    public void start() {
        try {
            if (this.component.isStopped()) {
                logger.trace("Initializing REST Server.");
                this.component.start();
                logger.debug("Indra is up and running!");
            } else {
                logger.warn("REST Server already started.");
            }
        } catch (Exception e) {
            logger.error("Failure staring REST Server.", e);
        }
    }

    public void stop() {
        try {
            if (this.component.isStarted()) {
                logger.info("Terminating REST Server.");
                this.component.stop();
            } else {
                logger.warn("REST Server already stopped.");
            }
        } catch (Exception e) {
            logger.error("Failure staring REST Server.", e);
        }
    }

    private void configureRoutes(Router router) {
        String base = configuration.httpBasePath;
        if (runOptions.mock) {
            logger.warn("Attention: MOCK mode is ON!");
            router.attach(String.format("%s/relatedness", base), MockedRelatednessResourceImpl.class);
        }
        else {
            router.attach(String.format("%s/relatedness", base), RelatednessResourceImpl.class);
        }
    }

}
