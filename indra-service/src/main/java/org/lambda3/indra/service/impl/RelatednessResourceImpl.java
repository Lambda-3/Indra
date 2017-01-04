package org.lambda3.indra.service.impl;

import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.RelatednessResult;
import org.lambda3.indra.core.impl.RelatednessClientFactory;
import org.lambda3.indra.rest.resources.RelatednessRequest;
import org.lambda3.indra.rest.resources.RelatednessResponse;
import org.lambda3.indra.service.config.Configuration;
import org.restlet.data.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: Add Authorization! Token based?
public class RelatednessResourceImpl extends RelatednessServerResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Configuration configuration;
    private static RelatednessClientFactory relatednessClientFactory;

    @Override
    public RelatednessResponse getRelatedness(RelatednessRequest request) {
        if (request == null) {
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }

        logger.trace("User Request: {}", request); //TODO: Maybe we should trunk to avoid logging long messages
        return process(request);
    }


    @Override
    protected void doCatch(Throwable throwable) {
        logger.error("Caught an exception..", throwable);
        super.doCatch(throwable);
    }

    @Override
    protected void doInit() {
        synchronized (RelatednessResourceImpl.class) {
            if (relatednessClientFactory == null) {
                configuration = (Configuration) getContext().getAttributes().get(Configuration.class.getCanonicalName());
                relatednessClientFactory = new RelatednessClientFactory(configuration.mongoURI);
            }
        }
    }

    private RelatednessResponse process(RelatednessRequest request) {

        Params params = buildParams(request);
        RelatednessClient client = relatednessClientFactory.create(params);
        RelatednessResult result = client.getRelatedness(request.pairs);

        RelatednessResponse response = new RelatednessResponse();
        response.corpus = request.corpus;
        response.language = request.language;
        response.model = request.model;
        response.scoreFunction = request.scoreFunction;
        response.pairs = result.getScores();

        //TODO: Maybe we should trunk to avoid logging long messages
        logger.trace("Response: {}", response);

        return response;
    }

    private static Params buildParams(RelatednessRequest req) {
        return new Params(req.corpus, req.scoreFunction, req.language, req.model);
    }
}
