package org.lambda3.indra.annoy;

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

import com.spotify.annoy.ANNIndex;
import com.spotify.annoy.AnnoyIndex;
import com.spotify.annoy.IndexType;
import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;
import org.lambda3.indra.core.CachedVectorSpace;
import org.lambda3.indra.core.composition.VectorComposer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class AnnoyVectorSpace extends CachedVectorSpace {

    public static final String INDEX_TYPE = "index-type";
    public static final String TREE_FILE = "tree";
    public static final String METADATA_FILE = "metadata.txt";
    public static final String WORD_MAPPING_FILE = "mappings.txt";

    private AnnoyIndex index;
    private String dataDir;
    private String[] idToWord;
    private Map<String, Integer> wordToId = new HashMap<>();

    public AnnoyVectorSpace(String dataDir, VectorComposer termComposer, VectorComposer translationComposer) {
        super(termComposer, translationComposer);
        Objects.nonNull(dataDir);
        this.dataDir = dataDir;
        this.metadata = loadMetadata();
        loadMappings();

        IndexType type = IndexType.valueOf(metadata.getMore().get(INDEX_TYPE));

        try {
            this.index = new ANNIndex(metadata.getDimensions(), new File(dataDir, TREE_FILE).getAbsolutePath(), type);
        } catch (IOException e) {
            logger.error("problem instantiating an ANNIndex.");
            e.printStackTrace();
        }
    }

    private void loadMappings() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(dataDir, WORD_MAPPING_FILE)));

            String headline = reader.readLine();
            int nWords = Integer.parseInt(headline);
            this.idToWord = new String[nWords];

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("|");
                int id = Integer.parseInt(parts[0]);
                this.idToWord[id] = parts[1];
                this.wordToId.put(parts[1], id);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
    protected void collectVectors(Collection<String> terms, int limit) {

    }

    @Override
    protected ModelMetadata loadMetadata() {


        return null;
    }

    @Override
    public LinkedHashMap<String, Double> getNearestNeighbors(AnalyzedTerm term, int topk) {
        return null;
    }

    @Override
    public LinkedHashMap<String, RealVector> getNearestVectors(AnalyzedTerm term, int topk) {

        return null;
    }
}
