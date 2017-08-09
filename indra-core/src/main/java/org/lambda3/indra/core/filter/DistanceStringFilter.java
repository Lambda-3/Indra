package org.lambda3.indra.core.filter;

import org.apache.commons.lang3.StringUtils;

public class DistanceStringFilter implements Filter {
    private int threshold;
    private int min;

    public DistanceStringFilter(int threshold, int min) {
        this.threshold = threshold;
        this.min = min;
    }

    @Override
    public boolean matches(String t1, String t2) {
        return t1.length() >= this.threshold && StringUtils.getLevenshteinDistance(t1, t2) < min;
    }
}
