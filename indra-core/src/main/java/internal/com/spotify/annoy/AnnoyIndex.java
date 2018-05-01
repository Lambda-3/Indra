package internal.com.spotify.annoy;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

        import java.io.Closeable;
        import java.util.List;

/**
 * AnnoyIndex interface, provided to aid with dependency injection in tests.
 */
public interface AnnoyIndex extends Closeable{
    /**
     * Get the vector for a given node's memory offset in the tree.
     * @param nodeOffset  node index in the ANN tree
     * @param v           output vector; overwritten.
     * @deprecated this should not be a public method
     */
    @Deprecated
    void getNodeVector(long nodeOffset, double[] v);

    /**
     * Get the vector for a given item in the tree.
     * @param itemIndex  item id
     * @param v          output vector; overwritten.
     * @deprecated use getItemVector(itemIndex)'s return value
     */
    @Deprecated
    void getItemVector(int itemIndex, double[] v);

    /**
     * Get the vector for a given item in the tree.
     * @param itemIndex  item id
     * @return item vector
     */
    double[] getItemVector(int itemIndex);

    /**
     * Look up nearest neighbors in the tree.
     * @param queryVector  find nearest neighbors for this query point
     * @param nResults     number of items to return
     * @return             list of items in descending nearness to query point
     */
    List<Integer> getNearest(double[] queryVector, int nResults);
}
