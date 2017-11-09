package org.lambda3.indra.filter;

import java.util.LinkedList;
import java.util.List;

public class FilterChain implements Filter {

    private List<Filter> filters = new LinkedList<>();

    private FilterChain() {

    }

    public static FilterChain get() {
        return new FilterChain();
    }

    public FilterChain add(Filter filter) {
        filters.add(filter);
        return this;
    }

    @Override
    public boolean matches(String t1, String t2) {
        for (Filter f : filters) {
            if (f.matches(t1, t2)) {
                return true;
            }
        }

        return false;
    }
}
