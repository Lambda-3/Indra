package org.lambda3.indra.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

public class VectorIterator<V extends Vector> implements Iterator<V> {

    private final boolean sparse;
    private Iterator<String> iterator;
    private int dimensions;

    public VectorIterator(File vectorsFile, long dimensions, Class<V> clazz) throws FileNotFoundException {
        this.sparse = clazz.equals(SparseVector.class);
        this.dimensions = (int) dimensions;
        this.iterator = new BufferedReader(new FileReader(vectorsFile)).lines().iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    @SuppressWarnings("unchecked")
    public V next() {
        String content = iterator.next();
        if (this.sparse) {
            return (V) new SparseVector(dimensions, content);
        } else {
            return (V) new DenseVector(content);
        }
    }
}
