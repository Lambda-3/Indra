package org.lambda3.indra.core.filter;

import java.util.Collection;

public interface Filter {

    void apply(Collection<String> terms);
}
