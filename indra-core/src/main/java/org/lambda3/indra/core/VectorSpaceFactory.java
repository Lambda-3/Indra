package org.lambda3.indra.core;

public abstract class VectorSpaceFactory<T extends VectorSpace> extends IndraCachedFactory<T> {
    protected VectorComposerFactory vectorComposerFactory;

    public VectorSpaceFactory() {
        this.vectorComposerFactory = new VectorComposerFactory();
    }
}
