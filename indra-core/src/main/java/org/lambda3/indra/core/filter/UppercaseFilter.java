package org.lambda3.indra.core.filter;

import java.util.Collection;

//https://stackoverflow.com/questions/4452939/in-java-how-to-find-if-first-character-in-a-string-is-upper-case-without-regex
//take a look in UTF-16 issues.

/**
 * Keeps in the collection only terms that starts with a uppercase character.
 */
public class UppercaseFilter implements Filter {

    @Override
    public void apply(Collection<String> terms) {
        terms.removeIf(t -> !Character.isUpperCase(t.charAt(0)));
    }
}
