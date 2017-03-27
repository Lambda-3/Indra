package org.lambda3.indra.core;


import org.lambda3.indra.core.composition.*;
import org.lambda3.indra.core.exception.IndraError;

import java.util.HashMap;
import java.util.Map;

public class VectorComposerFactory {

    private Map<VectorComposition, VectorComposer> statelessComposers = new HashMap<>();

    public VectorComposerFactory() {
        this.statelessComposers.put(VectorComposition.SUM, new SumVectorComposer());
        this.statelessComposers.put(VectorComposition.UNIQUE_SUM, new UniqueSumVectorComposer());
        this.statelessComposers.put(VectorComposition.AVERAGE, new AveragedVectorComposer());
    }

    public VectorComposer getComposer(VectorComposition name) {
        VectorComposer composer = statelessComposers.get(name);
        if (composer == null) {
            throw new IndraError("Unsupported vector composition.");
        }

        return composer;
    }
}
