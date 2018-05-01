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

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.io.LittleEndianDataInputStream;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.OpenMapRealVector;
import org.apache.commons.math3.linear.RealVector;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class VectorIterator implements Iterator<TermVector> {

    private final boolean sparse;
    private int dimensions;
    private int numberOfVectors;
    private LittleEndianDataInputStream input;
    private TermVector currentVector;

    public VectorIterator(File vectorsFile, long dimensions, boolean sparse) throws IOException {
        this.sparse = sparse;
        this.input = new LittleEndianDataInputStream(new FileInputStream(vectorsFile));
        parseHeadline(this.input);

        if (this.dimensions != (int) dimensions) {
            throw new IOException("inconsistent number of dimensions.");
        }

        setCurrentContent();
    }

    private void parseHeadline(LittleEndianDataInputStream input) throws IOException {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        byte c;

        while((c = input.readByte()) != '\n') {
            out.writeByte(c);
        }

        String[] headline = new String(out.toByteArray(), StandardCharsets.UTF_8).split(" ");
        this.numberOfVectors = Integer.parseInt(headline[0]);
        this.dimensions = Integer.parseInt(headline[1]);
    }

    private void setCurrentContent() throws IOException {
        if (numberOfVectors > 0) {
            numberOfVectors--;

            ByteArrayDataOutput out = ByteStreams.newDataOutput();

            byte b;
            while ((b = input.readByte()) != ' ') {
                out.writeByte(b);
            }

            String word = new String(out.toByteArray(), StandardCharsets.UTF_8);
            if (this.sparse) {
                this.currentVector = new TermVector(true, word, readSparseVector(this.dimensions));
            } else {
                this.currentVector = new TermVector(false, word, readDenseVector(this.dimensions));
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
    public TermVector next() {
        TermVector result = this.currentVector;
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
            vector.addToEntry(input.readInt(), (double) input.readFloat());
        }

        return vector;
    }

    public RealVector readDenseVector(int dimensions) throws IOException {
        double[] vector = new double[dimensions];

        for (int i = 0; i < dimensions; i++) {
            vector[i] = (double) input.readFloat();
        }

        return new ArrayRealVector(vector, false);
    }
}
