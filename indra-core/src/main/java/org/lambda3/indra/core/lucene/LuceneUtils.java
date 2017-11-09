package org.lambda3.indra.core.lucene;

import org.apache.commons.math3.linear.RealVector;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class LuceneUtils {
    private static Logger logger = LoggerFactory.getLogger(LuceneUtils.class);

    static TopDocs getTopDocs(IndexSearcher searcher, Iterable<? extends String> terms, String targetField) throws IOException {
        Map<String, RealVector> results = new LinkedHashMap<>();

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        for (String term : terms) {
            builder.add(new TermQuery(new Term(targetField, term)), BooleanClause.Occur.SHOULD);
        }

        Query query = builder.build();
        TotalHitCountCollector count = new TotalHitCountCollector();

        searcher.search(query, count);
        if (count.getTotalHits() > 0) {
            return searcher.search(query, count.getTotalHits());
        }

        return null;
    }
}
