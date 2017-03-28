package org.lambda3.indra.core.threading;

import org.lambda3.indra.client.AnalyzedPair;
import org.lambda3.indra.client.ScoredTextPair;
import org.lambda3.indra.client.TextPair;
import org.lambda3.indra.core.VectorPair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

//TODO part of multithreading capabilities - to continue...
public class RelatednessWorker implements Runnable {

    private Map<AnalyzedPair, VectorPair> vectorPairs;
    private List<AnalyzedPair> sortedPairs;
    private int from;
    private int to;
    private boolean sparse;
    private ConcurrentMap<TextPair, ScoredTextPair> output;

    public RelatednessWorker(Map<AnalyzedPair, VectorPair> vectorPairs, List<AnalyzedPair> sortedPairs, int from,
                             int to, boolean sparse, ConcurrentMap<TextPair, ScoredTextPair> output) {
        this.vectorPairs = vectorPairs;
        this.sortedPairs = sortedPairs;
        this.from = from;
        this.to = to;
        this.sparse = sparse;
        this.output = output;
    }

    @Override
    public void run() {

        for (int i = from; i <= to; i++) {
            AnalyzedPair pair = sortedPairs.get(i);
            VectorPair vectorPair = vectorPairs.get(pair);

            if (vectorPair.v1 != null && vectorPair.v2 != null) {

                if (!sparse) {
                    //output.put(pair.getTextPair(), new ScoredTextPair(pair,
                      //      sim(vectorPair.v1, vectorPair.v2, false)));
                } else {
                    //output.put(pair.getTextPair(), new ScoredTextPair(pair,
                         //   sim(vectorPair.v1, vectorPair.v2, true)));
                }

            } else {
                output.put(pair.getTextPair(), new ScoredTextPair(pair, 0));
            }

        }

    }
}
