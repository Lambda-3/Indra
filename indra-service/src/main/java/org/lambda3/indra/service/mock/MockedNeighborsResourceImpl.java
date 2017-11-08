package org.lambda3.indra.service.mock;

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

import org.apache.commons.math3.random.UnitSphereRandomVectorGenerator;
import org.lambda3.indra.NeighborsResource;
import org.lambda3.indra.request.NeighborRelatednessRequest;
import org.lambda3.indra.request.NeighborsVectorsRequest;
import org.lambda3.indra.response.DenseNeighborVectorsResponse;
import org.lambda3.indra.response.NeighborRelatednessResponse;
import org.lambda3.indra.response.NeighborVectorsResponse;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class MockedNeighborsResourceImpl implements NeighborsResource {

    private static final int NUM_DIMENSIONS = 300;
    private static final UnitSphereRandomVectorGenerator RVG = new UnitSphereRandomVectorGenerator(NUM_DIMENSIONS);
    private static final List<String> NEIGHBORS = Arrays.asList("love", "war", "mother", "father", "mock", "object",
            "whisky", "samba", "peace", "pope", "power", "capital", "silent", "water", "music", "blue", "tango", "car");

    @Override
    public NeighborVectorsResponse getNeighborsVectors(NeighborsVectorsRequest request) {
        Map<String, Map<String, double[]>> terms = new HashMap<>();

        for (String term : request.getTerms()) {
            Map<String, double[]> ns = new HashMap<>();
            for (String neighbor : getRandomWords()) {
                ns.put(neighbor, RVG.nextVector());
            }

            terms.put(term, ns);
        }

        return new DenseNeighborVectorsResponse(request, terms);
    }

    @Override
    public NeighborRelatednessResponse getNeighborRelatedness(NeighborRelatednessRequest request) {
        Map<String, Map<String, Double>> terms = new HashMap<>();

        for (String term : request.getTerms()) {
            Map<String, Double> ns = new HashMap<>();
            for (String neighbor : getRandomWords()) {
                ns.put(neighbor, ThreadLocalRandom.current().nextDouble(0, 1));
            }

            terms.put(term, ns);
        }

        return new NeighborRelatednessResponse(request, terms);
    }

    private Set<String> getRandomWords() {
        Set<String> results = new HashSet<>();

        int n = ThreadLocalRandom.current().nextInt(1, 8);
        for (int i = 0; i < n; i++) {
            int index = ThreadLocalRandom.current().nextInt(0, NEIGHBORS.size());
            results.add(NEIGHBORS.get(index));
        }

        return results;
    }
}
