package org.lambda3.indra.core.lucene;

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

import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.RealVectorUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.FSDirectory;
import org.lambda3.indra.AnalyzedTerm;
import org.lambda3.indra.MetadataIO;
import org.lambda3.indra.core.codecs.BinaryCodecs;
import org.lambda3.indra.core.utils.MapUtils;
import org.lambda3.indra.core.vs.AbstractVectorSpace;
import org.lambda3.indra.filter.Filter;
import org.lambda3.indra.model.ModelMetadata;
import org.lambda3.indra.relatedness.AbsoluteCosineRelatednessFunction;
import org.lambda3.indra.relatedness.RelatednessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LuceneVectorSpace extends AbstractVectorSpace {

    public static final String TERMS_INDEX = "terms";

    public static final String TERM_FIELD = "term";
    public static final String VECTOR_FIELD = "vector";
    public static final String INDEXES_FIELD = "indexes";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private String modelDir;
    private IndexReader termsReader;
    private IndexSearcher termsSearcher;
    private RelatednessFunction func = new AbsoluteCosineRelatednessFunction();

    static {
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
    }

    public LuceneVectorSpace(String modelDir) {
        this.modelDir = modelDir;
        try {
            this.termsReader = DirectoryReader.open(FSDirectory.open(Paths.get(modelDir, TERMS_INDEX)));
            this.termsSearcher = new IndexSearcher(termsReader);
        } catch (IOException e) {
            logger.error("Lucene basedir is wrongly configured or there is an I/O error\n{}", e.getMessage());
            //TODO throw exception
            e.printStackTrace();
        }
    }

    @Override
    protected ModelMetadata loadMetadata() {
        return MetadataIO.load(modelDir, ModelMetadata.class);
    }

    @Override
    protected Map<String, RealVector> collectVectors(Iterable<? extends String> terms) {
        return collectVectors(terms, TERM_FIELD);
    }

    private Map<String, RealVector> collectVectors(Iterable<? extends String> terms, String targetField) {
        Map<String, RealVector> results = new LinkedHashMap<>();

        try {
            TopDocs topDocs = LuceneUtils.getTopDocs(termsSearcher, terms, targetField);

            if (topDocs != null && topDocs.totalHits > 0) {
                for (ScoreDoc sd : topDocs.scoreDocs) {
                    Document doc = termsSearcher.doc(sd.doc);
                    RealVector v = BinaryCodecs.unmarshall(doc.getBinaryValue(VECTOR_FIELD).bytes, true, (int) metadata.dimensions);
                    results.put(doc.get(TERM_FIELD), v);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            //TODO throw new expection here.
        }

        return results;
    }


    @Override
    public Map<String, RealVector> getNearestVectors(AnalyzedTerm term, int topk, Filter filter) {
        TermQuery query = new TermQuery(new Term(term.getFirstToken()));

        try {
            TopDocs topDocs = termsSearcher.search(query, 1);

            if (topDocs.totalHits == 1) {
                Document doc = termsSearcher.doc(topDocs.scoreDocs[0].doc);
                String[] indexes = doc.getValues(INDEXES_FIELD);
                RealVector termVector = BinaryCodecs.unmarshall(doc.getBinaryValue(VECTOR_FIELD).bytes, true, -1);

                Map<String, RealVector> candidates = collectVectors(Arrays.asList(indexes), INDEXES_FIELD);
                Map<String, Double> results = new ConcurrentHashMap<>();

                candidates.entrySet().stream().parallel().forEach(e -> {
                    double score = func.sim(termVector, e.getValue(), true);
                    results.put(e.getKey(), score);
                });

                LinkedHashMap<String, Double> sortedResults = MapUtils.entriesSortedByValues(results);

                Map<String, RealVector> nearest = new LinkedHashMap<>();
                int count = 0;
                for (String key : sortedResults.keySet()) {
                    Map<Integer, Double> mapVector = RealVectorUtil.vectorToMap(candidates.get(key));
                    int size = (int) metadata.dimensions;
                    OpenMapRealVector vector = new OpenMapRealVector(size);

                    for (int index : mapVector.keySet()) {
                        vector.setEntry(index, mapVector.get(index));
                    }
                    nearest.put(key, vector);
                }

                return nearest;
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            //TODO throw new expection here.
        }

        return null;
    }

    @Override
    public Collection<String> getNearestTerms(AnalyzedTerm term, int topk, Filter filter) {
        Map<String, RealVector> terms = getNearestVectors(term, topk, filter);

        return terms.keySet();
    }

    @Override
    public void close() throws IOException {
        termsReader.close();
    }
}
