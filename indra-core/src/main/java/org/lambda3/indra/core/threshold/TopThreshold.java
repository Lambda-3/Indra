package org.lambda3.indra.core.threshold;

import org.lambda3.indra.Threshold;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class TopThreshold implements Threshold {

    private final int top;

    public TopThreshold(int top) {
        this.top = top;
    }

    @Override
    public void apply(LinkedHashMap<String, Double> relatedness) {

        int limit = top;
        Iterator<Map.Entry<String, Double>> iter = relatedness.entrySet().iterator();
        while (iter.hasNext() && limit-- > 0) {
            iter.next();
        }

        while (iter.hasNext()) {
            iter.next();
            iter.remove();
        }
    }
}
