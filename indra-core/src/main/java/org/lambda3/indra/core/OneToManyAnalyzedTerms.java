package org.lambda3.indra.core;

import org.lambda3.indra.client.AnalyzedTerm;

import java.util.List;

public class OneToManyAnalyzedTerms {

    public final AnalyzedTerm oneAnalyzedTerm;

    public final List<AnalyzedTerm> manyAnalyzedTerms;

    public OneToManyAnalyzedTerms(AnalyzedTerm oneAnalyzedTerm, List<AnalyzedTerm> manyAnalyzedTerms) {
        this.oneAnalyzedTerm = oneAnalyzedTerm;
        this.manyAnalyzedTerms = manyAnalyzedTerms;
    }
}
