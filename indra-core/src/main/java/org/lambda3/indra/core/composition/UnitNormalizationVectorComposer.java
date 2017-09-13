package org.lambda3.indra.core.composition;


import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public final class UnitNormalizationVectorComposer extends SumVectorComposer {
    @Override
    public RealVector compose(List<RealVector> vectors) {
        logger.trace("Composing {} vectors", vectors.size());
        RealVector sum = super.compose(vectors);
        if (vectors.size() > 1) {
            //norm is not calculated for single vectors.
            double norm = sum.getNorm();
            return sum.mapDivideToSelf(norm);
        }

        return sum;
    }
}
