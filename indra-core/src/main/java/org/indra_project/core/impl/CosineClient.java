package org.indra_project.core.impl;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import org.indra_project.core.Params;

public class CosineClient extends RelatednessBaseClient {

    CosineClient(Params params, MongoVectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        //TODO: Should be configurable?
        // Attention: May break the cache vectors if comes from client requests
        // Pay attention to the same behavior on the other clients.
        return 1500;
    }

    @Override
    protected double sim(double[] v1, double[] v2) {
        DoubleVector dv1 = Vectors.asVector(v1);
        DoubleVector dv2 = Vectors.asVector(v2);
        SimilarityFunction f = Similarity.getSimilarityFunction(Similarity.SimType.COSINE);
        return f.sim(dv1, dv2);
    }
}
