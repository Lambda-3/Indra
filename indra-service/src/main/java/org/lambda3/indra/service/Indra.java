package org.lambda3.indra.service;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import org.lambda3.indra.service.config.Configuration;
import org.lambda3.indra.service.config.RunOptions;
import org.lambda3.indra.service.impl.RESTServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Main Entry Point
 */
public final class Indra {
    static {
        // Routing log messages from restlet to slf4j
        System.setProperty("org.restlet.engine.loggerFacadeClass", "org.restlet.ext.slf4j.Slf4jLoggerFacade");
    }

    static Logger logger = LoggerFactory.getLogger(Indra.class);

    public static void main(String[] args) throws Exception {
        ArgumentParser parser = ArgumentParsers.newArgumentParser("Indra")
                .defaultHelp(true)
                .description("Starts the Indra REST Server");

        parser.addArgument("configFile")
                .type(File.class)
                .help("Further configurations.");

        parser.addArgument("-m", "--mock")
                .action(Arguments.storeConst())
                .setConst(true)
                .setDefault(false)
                .help("Mock relatedness resource returning random value.");

        parser.addArgument("-p", "--httpPort")
                .type(Integer.class)
                .setDefault(8916)
                .help("HTTP port to listen for requests.");

        RunOptions options = new RunOptions();

        try {
            parser.parseArgs(args, options);
            logger.info("Running Options:\n {}", options);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        run(options);
    }

    private static void run(RunOptions options) throws Exception {
        Configuration config = readConfiguration(options.configFile);
        RESTServer restServer = new RESTServer(options, config);
        restServer.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Going down..");
            restServer.stop();
        }));
    }

    private static Configuration readConfiguration(File configFile) throws IOException {
        if (configFile == null) {
            logger.info("Using default configuration.");
            return new Configuration();
        }
        logger.info("Reading configuration from {}", configFile.getAbsolutePath());
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        return mapper.readValue(configFile, Configuration.class);
    }

}
