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

import org.lambda3.indra.web.VectorResource;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.request.VectorRequest;
import org.lambda3.indra.response.DenseVectorResponse;
import org.lambda3.indra.response.SparseVectorResponse;
import org.lambda3.indra.response.VectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public final class VectorResourceImpl implements VectorResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private IndraDriver driver;

    public VectorResourceImpl(IndraDriver driver) {
        this.driver = Objects.requireNonNull(driver);
    }

    @Override
    public VectorResponse getVector(VectorRequest request) {
        logger.trace("getVector - User Request: {}", request);
        RequestValidator.validate(request);

        VectorResponse response;

        if (this.driver.isSparseModel(request)) {
            Map<String, Map<Integer, Double>> results = this.driver.getVectorsAsMap(request);
            response = new SparseVectorResponse(request, results);
        } else {
            Map<String, double[]> results = this.driver.getVectorsAsArray(request);
            response = new DenseVectorResponse(request, results);
        }

        logger.trace("Response: {}", response);
        return response;
    }
}
