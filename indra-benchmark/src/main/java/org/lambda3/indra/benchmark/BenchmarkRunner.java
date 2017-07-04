package org.lambda3.indra.benchmark;

/*-
 * ==========================License-Start=============================
 * Indra Benchmark Module
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
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.core.VectorSpace;
import org.lambda3.indra.core.VectorSpaceFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class BenchmarkRunner {

    private static final String WORD_SET_1_FILE = "wordSet1.txt";
    private static final String WORD_SET_2_FILE = "wordSet2.txt";
    private static final String WORD_SET_3_FILE = "wordSet3.txt";

    private VectorSpaceFactory factory1;
    private VectorSpaceFactory factory2;
    private Report report;

    public BenchmarkRunner(VectorSpaceFactory factory1, VectorSpaceFactory factory2) {
        this.factory1 = factory1;
        this.factory2 = factory2;
        this.report = new Report(String.format("==== Benchmark - %s vs %s",
                factory1.getClass().getSimpleName().replace("Factory", ""),
                factory2.getClass().getSimpleName().replace("Factory", "")));
    }

    private static Set<String> loadWordSet(String file) {

        InputStream stream = BenchmarkRunner.class.getClassLoader().getResourceAsStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        Set<String> set = new HashSet<>();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                set.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return set;
    }

    public void run(String file, IndraAnalyzer analyzer, VectorSpace vs1, VectorSpace vs2) {
        Set<String> words = loadWordSet(file);

        List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
        for (String term : words) {
            analyzedTerms.add(new AnalyzedTerm(term, analyzer.analyze(term)));
        }

        double start1 = System.currentTimeMillis();
        Map<String, RealVector> vectors1 = vs1.getVectors(analyzedTerms);
        double end1 = System.currentTimeMillis();

        double start2 = System.currentTimeMillis();
        Map<String, RealVector> vectors2 = vs2.getVectors(analyzedTerms);
        double end2 = System.currentTimeMillis();

        this.report.addInfo(file, vs1.getClass().getSimpleName(), end1 - start1,
                vs2.getClass().getSimpleName(), end2 - start2);

    }

    public static void main(String[] args) {
        String mongoService = System.getProperty("indra.mongoURI");
    }
}
