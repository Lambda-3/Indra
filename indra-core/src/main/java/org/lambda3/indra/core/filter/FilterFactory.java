package org.lambda3.indra.core.filter;

import org.lambda3.indra.client.FilterType;

public class FilterFactory {

    public static Filter create(FilterType type, float param) {
        switch (type) {
            case AUTO:
                return new AutoFilter();
            case UPPERCASE:
                return new UppercaseFilter();
            case MAX:
                return new MaxFilter(param);
            case MIN:
                return new MinFilter(param);
            default:
                throw new RuntimeException("not supported FilterType");
        }
    }
}
