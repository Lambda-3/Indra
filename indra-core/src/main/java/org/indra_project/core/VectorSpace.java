package org.indra_project.core;

import org.indra_project.common.client.AnalyzedPair;

import java.util.List;
import java.util.Map;

//TODO: JavaDoc
public interface VectorSpace {

    VectorPair getVector(AnalyzedPair pair);

    VectorPair getVector(AnalyzedPair pair, int limit);

    Map<AnalyzedPair, VectorPair> getVectors(List<AnalyzedPair> terms, int limit);
}
