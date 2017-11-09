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
import org.lambda3.indra.AnalyzedTerm;
import org.lambda3.indra.annoy.AnnoyVectorSpaceFactory;
import org.lambda3.indra.core.IndraAnalyzer;
import org.lambda3.indra.core.vs.VectorSpace;
import org.lambda3.indra.core.vs.VectorSpaceFactory;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.lambda3.indra.composition.SumVectorComposer;
import org.lambda3.indra.mongo.MongoVectorSpaceFactory;
import org.lambda3.indra.request.VectorRequest;

import java.io.*;
import java.util.*;

final class BenchmarkRunner {

    private static final String WORD_SET_1_FILE = "wordSet1-%s.txt";
    private static final String WORD_SET_2_FILE = "wordSet2-%s.txt";
    private static final String WORD_SET_3_FILE = "wordSet3-%s.txt";

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

    private void run(String file, IndraAnalyzer analyzer, VectorSpace vs1, VectorSpace vs2) {
        Set<String> words = loadWordSet(file);

        List<AnalyzedTerm> analyzedTerms = new LinkedList<>();
        for (String term : words) {
            analyzedTerms.add(new AnalyzedTerm(term, analyzer.analyze(term)));
        }

        double start1 = System.currentTimeMillis();
        Map<String, RealVector> vectors1 = vs1.getVectors(analyzedTerms, new SumVectorComposer());
        double end1 = System.currentTimeMillis();

        double start2 = System.currentTimeMillis();
        Map<String, RealVector> vectors2 = vs2.getVectors(analyzedTerms, new SumVectorComposer());
        double end2 = System.currentTimeMillis();

        this.report.addInfo(file, vs1.getClass().getSimpleName(), end1 - start1,
                vs2.getClass().getSimpleName(), end2 - start2);

        if (vectors1.size() == vectors2.size()) {
            for (String term : vectors1.keySet()) {
                RealVector v1 = vectors1.get(term);
                RealVector v2 = vectors2.get(term);

                if (!((v1 == null && v2 == null) || v1.equals(v2))) {
                    System.out.println(String.format("ERROR: %s - %s", v1, v2));
                    System.exit(-7);
                }
            }
        } else {
            System.out.println(String.format("ERROR: different size %d - %d", vectors1.size(), vectors2.size()));
        }
    }

    public Report run(String lang) {
        CorpusMetadata cm = CorpusMetadataBuilder.newCorpusMetadata("wiki-2014", lang).build();
        VectorRequest request = new VectorRequest();
        request.model("W2V").language(lang).corpus("wiki-2014").mt(false);

        IndraAnalyzer analyzer = new IndraAnalyzer(cm);
        String[] files = {WORD_SET_1_FILE, WORD_SET_2_FILE, WORD_SET_3_FILE};

        for (String file : files) {
            run(String.format(file, lang.toLowerCase()), analyzer, factory1.create(request), factory2.create(request));
        }

        return report;
    }

    public static void main(String[] args) {
        System.out.println("BenchmarkRunner v. 0.6");

        String mongoServer = System.getProperty("indra.mongoURI");
        String annoyDir = System.getProperty("indra.annoyBaseDir");
        int times = Integer.parseInt(System.getProperty("indra.benchmark.times", "3"));

        String[] langs = {"EN", "PT"};
        for (String lang : langs) {
            for (int i = 0; i < times; i++) {
                MongoVectorSpaceFactory mongoFactory = new MongoVectorSpaceFactory(mongoServer);
                AnnoyVectorSpaceFactory annoyFactory = new AnnoyVectorSpaceFactory(new File(annoyDir));

                BenchmarkRunner runner = new BenchmarkRunner(mongoFactory, annoyFactory);
                System.out.println(runner.run(lang));

                try {
                    mongoFactory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
