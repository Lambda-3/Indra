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

import org.lambda3.indra.client.*;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.RelatednessResult;
import org.lambda3.indra.core.utils.ParamsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

class IndraResourceImpl implements RelatednessResource, VectorResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private IndraDriver driver;

    IndraResourceImpl(IndraDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("driver can't be null.");
        }
        this.driver = driver;
    }

    @Override
    public RelatednessResponse getRelatedness(RelatednessRequest request) {
        logger.trace("getRelatedness - User Request: {}", request);
        return process(request, false);
    }


    @Override
    public RelatednessResponse getTranslatedRelatedness(RelatednessRequest request) {
        logger.trace("getTranslatedRelatedness - User Request: {}", request);
        return process(request, true);
    }

    private RelatednessResponse process(RelatednessRequest request, boolean translate) {
        Params params = ParamsUtils.buildParams(request, translate);
        RelatednessResult result = this.driver.getRelatedness(request.getPairs(), params);
        RelatednessResponse response = new RelatednessResponse(request, result.getScores());
        logger.trace("Response: {}", response);

        return response;
    }

    @Override
    public VectorResponse getVector(VectorRequest request) {
        return process(request, false);
    }

    @Override
    public VectorResponse getTranslatedVector(VectorRequest request) {
        return process(request, true);
    }

    public VectorResponse process(VectorRequest request, boolean translate) {
        logger.trace("getVector - User Request: {}", request);
        Params params = ParamsUtils.buildParams(request, translate);
        Map<String, Map<Integer, Double>> results = this.driver.getVectorsAsMap(request.getTerms(), params);

        VectorResponse response = new VectorResponse(request, results);
        logger.trace("Response: {}", response);
        return response;
    }
}
