package org.indra_project.core.impl;

import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.similarity.SimilarityFunction;
import edu.ucla.sspace.vector.DoubleVector;
import edu.ucla.sspace.vector.Vectors;
import org.indra_project.core.Params;

public class CorrelationClient extends RelatednessBaseClient {

    CorrelationClient(Params params, MongoVectorSpace vectorSpace) {
        super(params, vectorSpace);
    }

    @Override
    protected int getVectorSizeLimit() {
        return 1500;
    }

    @Override
    protected double sim(double[] v1, double[] v2) {
        //TODO: This can raise an error if the vectors has different sizes!
        DoubleVector dv1 = Vectors.asVector(v1);
        DoubleVector dv2 = Vectors.asVector(v2);
        SimilarityFunction f = Similarity.getSimilarityFunction(Similarity.SimType.PEARSON_CORRELATION);
        return f.sim(dv1, dv2);
    }
}
