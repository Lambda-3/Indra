package org.lambda3.indra.core.annoy;

/*-
 * ==========================License-Start=============================
 * Indra Annoy Module
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

import internal.com.spotify.annoy.ANNIndex;
import internal.com.spotify.annoy.AnnoyIndex;
import internal.com.spotify.annoy.IndexType;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.AnalyzedTerm;
import org.lambda3.indra.MetadataIO;
import org.lambda3.indra.core.vs.AbstractVectorSpace;
import org.lambda3.indra.filter.Filter;
import org.lambda3.indra.model.ModelMetadata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;


public class AnnoyVectorSpace extends AbstractVectorSpace {

    public static final String TREE_FILE = "trees.ann";
    public static final String WORD_MAPPING_FILE = "mappings.txt";

    private static final float TOP_FILTER_FACTOR = 2.5f;

    private AnnoyIndex index;
    private String dataDir;
    private String[] idToWord;
    private Map<String, Integer> wordToId = new ConcurrentHashMap<>();

    public AnnoyVectorSpace(String dataDir) {
        this.dataDir = Objects.requireNonNull(dataDir);
        loadMappings();

        IndexType type = IndexType.ANGULAR;

        try {
            this.index = new ANNIndex((int) getMetadata().dimensions, new File(dataDir, TREE_FILE).getAbsolutePath(), type);
        } catch (IOException e) {
            String msg = "problem instantiating an ANNIndex.";
            logger.error(msg);
            throw new RuntimeException(msg, e);
        }
    }

    private void loadMappings() {
        logger.trace("loading mappings from {}", dataDir);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dataDir, WORD_MAPPING_FILE)));

            String headline = reader.readLine();
            int nWords = Integer.parseInt(headline);
            this.idToWord = new String[nWords];

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(Pattern.quote("|"));
                int id = Integer.parseInt(parts[0]);
                this.idToWord[id] = parts[1];
                this.wordToId.put(parts[1], id);
            }
        } catch (IOException e) {
            String msg = String.format("errors when loading mappings. BASEDIR=%s | MAPPING_GILE=%s", dataDir, WORD_MAPPING_FILE);
            logger.error(msg);
            throw new RuntimeException(msg, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    protected ModelMetadata loadMetadata() {
        return MetadataIO.load(dataDir, ModelMetadata.class);
    }

    @Override
    protected Map<String, RealVector> collectVectors(Iterable<? extends String> terms) {
        Map<String, RealVector> vectors = new HashMap<>();

        for (String term : terms) {
            double[] v = getVector(term);
            if (v != null) {
                RealVector vector = new ArrayRealVector(v, false);
                vectors.put(term, vector);
            }
        }

        return vectors;
    }

    @Override
    public Map<String, RealVector> getNearestVectors(AnalyzedTerm term, int topk, Filter filter) {
        Collection<Integer> nearest = getNearestIds(term, topk, filter);

        Map<String, RealVector> results = new HashMap<>();
        for (Integer id : nearest) {
            double[] dv = index.getItemVector(id);

            RealVector vector = new ArrayRealVector(dv);
            results.put(idToWord[id], vector);
        }

        return results;
    }

    @Override
    public Collection<String> getNearestTerms(AnalyzedTerm term, int topk, Filter filter) {
        Collection<Integer> nearest = getNearestIds(term, topk, filter);
        Collection<String> terms = new LinkedList<>();

        for (Integer id : nearest) {
            terms.add(idToWord[id]);
        }

        return terms;
    }

    private Collection<Integer> getNearestIds(AnalyzedTerm term, int topk, Filter filter) {
        if (term.getAnalyzedTokens().size() == 1) {
            double[] vector = getVector(term.getFirstToken());

            if (vector != null) {
                if (filter == null) {
                    return this.index.getNearest(vector, topk);
                } else {
                    List<Integer> nearest = this.index.getNearest(vector, (int) (topk * TOP_FILTER_FACTOR));
                    int counter = 0;
                    Iterator<Integer> iter = nearest.iterator();
                    while (iter.hasNext()) {
                        int i = iter.next();
                        if (filter.matches(term.getFirstToken(), idToWord[i])) {
                            iter.remove();
                        } else {
                            counter++;
                        }

                        if (counter == topk) {
                            break;
                        }
                    }

                    return nearest.subList(0, Math.min(topk, nearest.size()));
                }
            }

            return Collections.emptyList();
        } else {
            String message = "NearestFunction is available only for single-token terms.";
            logger.error(message);
            throw new IllegalArgumentException(message);
        }
    }

    private double[] getVector(String term) {
        Integer termId = this.wordToId.get(term);
        if (termId != null) {
            return index.getItemVector(termId);
        } else {
            return null;
        }

    }

    @Override
    public void close() throws IOException {
        this.index.close();
    }
}
