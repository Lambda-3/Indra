package internal.com.spotify.annoy;

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