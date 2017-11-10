package org.lambda3.indra.util;

import org.lambda3.indra.model.ModelMetadata;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class RawSpaceModel<V extends Vector> {

    public static final String MODEL_CONTENT_FILE_NAME = "vectors.txt";

    public final ModelMetadata modelMetadata;
    private File vectorFileAbsolutePath;
    private Class<V> clazz;

    public RawSpaceModel(String baseDir, ModelMetadata modelMetadata, Class<V> clazz) {
        this.vectorFileAbsolutePath = Paths.get(baseDir, MODEL_CONTENT_FILE_NAME).toFile();
        this.modelMetadata = modelMetadata;
        this.clazz = clazz;
    }

    public boolean isSparse() {
        return modelMetadata.sparse;
    }

    public VectorIterator<V> getVectorIterator() throws FileNotFoundException {
        return new VectorIterator<V>(this.vectorFileAbsolutePath, modelMetadata.dimensions, clazz);
    }
}
