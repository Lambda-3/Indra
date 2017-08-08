package org.lambda3.indra.core.filter;

import java.util.Collection;

/**
 * Keeps in the collection only terms that starts with a lowercase character.
 */
public class LowercaseFilter implements Filter {
    @Override
    public void apply(Collection<String> terms) {
        terms.removeIf(t -> !Character.isLowerCase(t.charAt(0)));
    }
}
