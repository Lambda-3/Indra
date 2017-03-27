package org.lambda3.indra.core;

import org.lambda3.indra.core.exception.ModelNoFound;

public abstract class VectorSpaceFactory<T extends VectorSpace> {

    protected VectorComposerFactory vectorComposerFactory;

    public VectorSpaceFactory(VectorComposerFactory vectorComposerFactory) {
        if (vectorComposerFactory == null) {
            throw new IllegalArgumentException("vectorComposerFactory can't be null");
        }
        this.vectorComposerFactory = vectorComposerFactory;
    }

    public abstract T create(Params params) throws ModelNoFound;

}
