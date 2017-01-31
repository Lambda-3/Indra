package org.lambda3.indra.core.impl;

import org.lambda3.indra.core.Params;
import org.lambda3.indra.core.VectorSpace;
import org.lambda3.indra.core.exception.ModelNoFound;

public abstract class VectorSpaceFactory<T extends VectorSpace> {

    public abstract T create(Params params) throws ModelNoFound;

}
