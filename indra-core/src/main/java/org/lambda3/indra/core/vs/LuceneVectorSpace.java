package org.lambda3.indra.core.vs;

import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class LuceneVectorSpace extends CachedVectorSpace {

    public LuceneVectorSpace(String dataDir) {

    }

    @Override
    public Map<String, RealVector> loadAll(Iterable<? extends String> keys) throws Exception {
        return null;
    }

    @Override
    protected ModelMetadata loadMetadata() {
        return null;
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk) {
        return null;
    }

    @Override
    public Collection<String> getNearestTerms(AnalyzedTerm term, int topk) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }
}
