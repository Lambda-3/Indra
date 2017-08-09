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
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;
import org.lambda3.indra.core.filter.Filter;
import org.lambda3.indra.core.vs.CachedVectorSpace;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class AnnoyVectorSpace extends CachedVectorSpace {

    public static final String INDEX_TYPE = "index-type";
    public static final String TREE_FILE = "trees.ann";
    public static final String METADATA_FILE = "metadata.json";
    public static final String WORD_MAPPING_FILE = "mappings.txt";

    private static final float TOP_FILTER_FACTOR = 2.5f;

    private AnnoyIndex index;
    private String dataDir;
    private String[] idToWord;
    private Map<String, Integer> wordToId = new ConcurrentHashMap<>();

    public AnnoyVectorSpace(String dataDir) {
        this.dataDir = Objects.requireNonNull(dataDir);
        this.metadata = loadMetadata();
        loadMappings();

        IndexType type = IndexType.valueOf(metadata.getMore().getOrDefault(INDEX_TYPE, "ANGULAR"));

        try {
            this.index = new ANNIndex(metadata.getDimensions(), new File(dataDir, TREE_FILE).getAbsolutePath(), type);
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
            String msg = "errors when loading mappings.";
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
    public Map<String, Optional<RealVector>> loadAll(Iterable<? extends String> keys) throws Exception {
        logger.trace("loading all terms ...");
        Map<String, Optional<RealVector>> results = new HashMap<>();
        Stream<? extends String> stream = StreamSupport.stream(keys.spliterator(), true);

        stream.forEach(key -> {
            float[] vector = getVector(key);
            if (vector != null) {
                ArrayRealVector rVector = new ArrayRealVector(vector.length);
                for (int i = 0; i < vector.length; i++) {
                    rVector.addToEntry(i, vector[i]);
                }
                results.put(key, Optional.of(rVector));
            } else {
                results.put(key, Optional.empty());
            }
        });

        return results;
    }

    @Override
    protected ModelMetadata loadMetadata() {

        File file = new File(dataDir, METADATA_FILE);

        if (file.exists()) {
            try {
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(file));
                return ModelMetadata.createFromMap(jsonObject);

            } catch (ParseException | IOException e) {
                String msg = String.format("problem reading the metadata file. dataDir=%s", dataDir);
                logger.error(msg);
                throw new RuntimeException(msg, e);
            }

        } else {
            return ModelMetadata.createDefault();
        }
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk, Filter filter) {
        Collection<Integer> nearest = getNearestIds(term, topk, filter);

        Map<String, float[]> results = new HashMap<>();
        for (Integer id : nearest) {
            results.put(idToWord[id], index.getItemVector(id));
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
            float[] vector = getVector(term.getFirstToken());

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

    private float[] getVector(String term) {
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
