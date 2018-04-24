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

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;

public class VectorIterator implements Iterator<Vector> {

    private final boolean sparse;
    private int dimensions;
    private DataInputStream input;
    private Vector currentVector;

    public VectorIterator(File vectorsFile, long dimensions, boolean sparse) throws IOException {
        this.sparse = sparse;
        this.dimensions = (int) dimensions;

        this.input = new DataInputStream(new FileInputStream(vectorsFile));
    }

    private void setCurrentContent() throws IOException {
        StringBuilder sb = new StringBuilder();

        if (input.available() > 0) {
            char b;
            while ((b = (char) input.read()) != ' ') {
                sb.append(b);
            }

            if (this.sparse) {
                this.currentVector = new Vector(true, sb.toString(), readSparseVector(this.dimensions));
            } else {
                this.currentVector = new Vector(false, sb.toString(), readDenseVector(this.dimensions));
            }

        } else {
            this.currentVector = null;
        }
    }

    @Override
    public boolean hasNext() {
        return currentVector != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Vector next() {
        Vector result = this.currentVector;
        try {
            setCurrentContent();
        } catch (IOException e) {
            //TODO solve me.
            e.printStackTrace();
        }

        return result;
    }

    public RealVector readSparseVector(int dimensions) throws IOException {
        RealVector vector = new OpenMapRealVector(dimensions);

        int size = input.readInt();
        for (int i = 0; i < size; i++) {
            vector.addToEntry(input.readInt(), input.readFloat());
        }

        input.readChar(); //character \n in the end.
        return vector;
    }

    public RealVector readDenseVector(int dimensions) throws IOException {
        double[] vector = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            vector[i] = input.readFloat();
        }

        input.readChar(); //character \n in the end.
        return new ArrayRealVector(vector, false);
    }
}
