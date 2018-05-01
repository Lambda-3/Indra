package org.lambda3.indra.core.vs;

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

import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.AnalyzedPair;
import org.lambda3.indra.AnalyzedTerm;
import org.lambda3.indra.AnalyzedTranslatedPair;
import org.lambda3.indra.MutableTranslatedTerm;
import org.lambda3.indra.composition.VectorComposer;
import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.core.VectorPair;
import org.lambda3.indra.filter.Filter;
import org.lambda3.indra.model.ModelMetadata;

import java.io.Closeable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface VectorSpace extends Closeable {

    Map<AnalyzedPair, VectorPair> getVectorPairs(List<AnalyzedPair> pairs, VectorComposer termComposer);

    Map<AnalyzedTranslatedPair, VectorPair> getTranslatedVectorPairs(List<AnalyzedTranslatedPair> pairs,
                                                                     VectorComposer termComposer, VectorComposer translationComposer);

    Map<String, RealVector> getVectors(List<AnalyzedTerm> terms, VectorComposer termComposer);

    Map<String, RealVector> getTranslatedVectors(List<MutableTranslatedTerm> terms, VectorComposer termComposer,
                                                 VectorComposer translationComposer);

    Map<String, RealVector> getNearestVectors(AnalyzedTerm term, int topk, Filter filter);

    Collection<String> getNearestTerms(AnalyzedTerm term, int topk, Filter filter);

    Collection<String> getNearestTerms(double[] vector, int topk);

    ModelMetadata getMetadata();

    IndraAnalyzer getAnalyzer();
}
