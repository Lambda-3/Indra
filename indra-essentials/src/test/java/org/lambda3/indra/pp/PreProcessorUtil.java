package org.lambda3.indra.pp;

import java.util.Iterator;

public class PreProcessorUtil {

    public static String toString(Iterator<String> tokens) {
        Iterable<String> iterable = () -> tokens;
        return String.join(" ", iterable);
    }
}
