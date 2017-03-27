package org.lambda3.indra.core.composition;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class UniqueSumVectorComposer extends SumVectorComposer {

    @Override
    public Collection<String> filter(List<String> terms) {
        return new HashSet<>(terms);
    }
}
