package org.lambda3.indra.core;

import org.lambda3.indra.common.client.AnalyzedPair;

import java.util.List;
import java.util.Map;

public interface VectorSpace {

    VectorPair getVector(AnalyzedPair pair);

    VectorPair getVector(AnalyzedPair pair, int limit);

    Map<AnalyzedPair, VectorPair> getVectors(List<AnalyzedPair> terms, int limit);
}
