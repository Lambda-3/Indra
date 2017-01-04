package org.indra_project.core;

import com.google.common.base.Preconditions;
import org.indra_project.common.client.ScoredTextPair;
import org.indra_project.common.client.TextPair;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RelatednessResult {
    private Map<TextPair, ScoredTextPair> results;

    public RelatednessResult(Collection<ScoredTextPair> scoredTerms) {
        Preconditions.checkNotNull(scoredTerms);
        results = new LinkedHashMap<>();
        for (ScoredTextPair scoredTextPair : scoredTerms) {
            results.put(new TextPair(scoredTextPair.t1, scoredTextPair.t2), scoredTextPair);
        }
    }

    public ScoredTextPair getScore(TextPair pair) {
        return results.get(pair);
    }

    public Collection<ScoredTextPair> getScores() {
        return results.values();
    }
}
