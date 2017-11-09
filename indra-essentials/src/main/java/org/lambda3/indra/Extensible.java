package org.lambda3.indra;

public interface Extensible {

    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }
}