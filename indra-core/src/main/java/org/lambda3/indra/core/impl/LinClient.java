package org.lambda3.indra.core.impl;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import org.lambda3.indra.core.Params;

public class LinClient extends RelatednessBaseClient {

    LinClient(Params params, MongoVectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        return 1500;
    }

    @Override
    protected double sim(double[] v1, double[] v2) {
        DoubleVector dv1 = Vectors.asVector(v1);
        DoubleVector dv2 = Vectors.asVector(v2);
        SimilarityFunction f = Similarity.getSimilarityFunction(Similarity.SimType.LIN);
        return f.sim(dv1, dv2);
    }
}
