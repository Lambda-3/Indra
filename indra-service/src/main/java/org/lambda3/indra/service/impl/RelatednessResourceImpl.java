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

import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessClient;
import org.lambda3.indra.core.RelatednessResult;
import org.lambda3.indra.core.RelatednessClientFactory;
import org.lambda3.indra.core.VectorSpaceFactory;
import org.lambda3.indra.client.RelatednessRequest;
import org.lambda3.indra.client.RelatednessResource;
import org.lambda3.indra.client.RelatednessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class RelatednessResourceImpl implements RelatednessResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private RelatednessClientFactory relatednessClientFactory;

    RelatednessResourceImpl(VectorSpaceFactory vectorSpaceFactory) {
        this.relatednessClientFactory = new RelatednessClientFactory(vectorSpaceFactory);
    }

    @Override
    public RelatednessResponse getRelatedness(RelatednessRequest request) {
        logger.trace("User Request: {}", request);
        return process(request);
    }

    private RelatednessResponse process(RelatednessRequest request) {
        Params params = buildParams(request);
        RelatednessClient client = relatednessClientFactory.create(params);
        RelatednessResult result = client.getRelatedness(request.getPairs());
        RelatednessResponse response = new RelatednessResponse(request, result.getScores());
        logger.trace("Response: {}", response);
        return response;
    }

    private static Params buildParams(RelatednessRequest req) {
        return new Params(req.getCorpus(), req.getScoreFunction(), req.getLanguage(), req.getModel());
    }
}
