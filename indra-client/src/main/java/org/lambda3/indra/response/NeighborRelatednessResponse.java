package org.lambda3.indra.response;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
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

import org.lambda3.indra.request.NeighborRelatednessRequest;

import java.util.Map;
import java.util.Objects;

public class NeighborRelatednessResponse extends AbstractBasicResponse {
    private String scoreFunction;
    private int topk;
    private Map<String, Map<String, Double>> terms;

    protected NeighborRelatednessResponse() {
        //jersey demands.
    }

    public NeighborRelatednessResponse(NeighborRelatednessRequest request, Map<String, Map<String, Double>> terms) {
        super(request);
        this.scoreFunction = request.getScoreFunction();
        this.topk = request.getTopk();
        this.terms = Objects.requireNonNull(terms);
    }

    public Map<String, Map<String, Double>> getTerms() {
        return terms;
    }

    public int getTopk() {
        return this.topk;
    }

    @Override
    public String toString() {
        return "NeighborRelatednessResponse{" +
                "scoreFunction=" + scoreFunction +
                ", terms size=" + terms.size() +
                '}';
    }
}
