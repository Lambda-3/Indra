package org.lambda3.indra.core.vs;

import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.lambda3.indra.client.AnalyzedTerm;
import org.lambda3.indra.client.ModelMetadata;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class LuceneVectorSpace extends CachedVectorSpace {

    private static final String TERM_INDEX = "terms";
    private static final String METADATA_INDEX = "metadata";

    private static final String TERM_FIELD = "term";
    private static final String KEYS_FIELD = "keys";
    private static final String VECTOR_FIELD = "vector";

    static {
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
    }

    private IndexSearcher termsSearcher;
    private IndexSearcher metadataSearcher;

    public LuceneVectorSpace(String dataDir) {
        try {
            Directory dir = FSDirectory.open(Paths.get(dataDir));
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Map<String, Optional<RealVector>> loadAll(Iterable<? extends String> keys) throws Exception {

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String k : keys) {
            builder.add(new TermQuery(new Term(TERM_FIELD, k)), BooleanClause.Occur.SHOULD);
        }

        BooleanQuery query = builder.build();
        TopDocs hits = termsSearcher.search(query, Integer.MAX_VALUE);

        int num = 0;
        for (ScoreDoc sd : hits.scoreDocs) {
            Document doc = termsSearcher.doc(sd.doc);
            //TODO load vector here.
        }

        return null;
    }

    @Override
    protected ModelMetadata loadMetadata() {
        return null;
    }

    @Override
    public Map<String, float[]> getNearestVectors(AnalyzedTerm term, int topk) {
        Collection<String> nn = getNearestTerms(term, topk);
        //Map<String, RealVector> vectors = collectVectors(nn);

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
