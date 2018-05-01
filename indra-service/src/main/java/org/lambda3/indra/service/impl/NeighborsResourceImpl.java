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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorUtil;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.request.NeighborRelatednessRequest;
import org.lambda3.indra.request.NeighborsByVectorRequest;
import org.lambda3.indra.request.NeighborsVectorsRequest;
import org.lambda3.indra.response.*;
import org.lambda3.indra.web.NeighborsResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

public final class NeighborsResourceImpl implements NeighborsResource {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private IndraDriver driver;

    public NeighborsResourceImpl(IndraDriver driver) {
        this.driver = Objects.requireNonNull(driver);
    }

    @Override
    public NeighborVectorsResponse getNeighborsVectors(NeighborsVectorsRequest request) {
        logger.trace("User Request: {}", request);

        RequestValidator.validate(request);
        Map<String, Map<String, RealVector>> vectors = this.driver.getNeighborsVectors(request);
        boolean sparse = true;
        if (!vectors.isEmpty()) {
            Map<String, RealVector> map = vectors.values().iterator().next();
            if (!map.isEmpty()) {
                RealVector vector = map.values().iterator().next();
                if (vector instanceof ArrayRealVector) {
                    sparse = false;
                }
            }
        }


        NeighborVectorsResponse response;
        if (sparse) {
            Map<String, Map<String, Map<Integer, Double>>> sparseVectors = RealVectorUtil.convertAllToMap(vectors);
            response = new SparseNeighborVectorsResponse(request, sparseVectors);
        } else {
            Map<String, Map<String, double[]>> arrayVectors = RealVectorUtil.convertAllToArray(vectors);
            response = new DenseNeighborVectorsResponse(request, arrayVectors);
        }

        logger.trace("Response: {}", response);

        return response;
    }

    @Override
    public NeighborRelatednessResponse getNeighborRelatedness(NeighborRelatednessRequest request) {
        logger.trace("User Request: {}", request);
        RequestValidator.validate(request);

        Map<String, Map<String, Double>> relatedness = this.driver.getNeighborRelatedness(request);
        NeighborRelatednessResponse response = new NeighborRelatednessResponse(request, relatedness);
        logger.trace("Response: {}", response);

        return response;
    }

    @Override
    public NeighborsByVectorResponse getNeighborsByVector(NeighborsByVectorRequest request) {
        logger.trace("User Request: {}", request);
        RequestValidator.validate(request);

        Collection<String> terms = this.driver.getNeighborsByVector(request);
        NeighborsByVectorResponse response = new NeighborsByVectorResponse(request, terms);
        logger.trace("Response: {}", response);

        return response;
    }
}
