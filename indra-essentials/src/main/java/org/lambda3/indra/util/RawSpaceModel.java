package org.lambda3.indra.util;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

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
