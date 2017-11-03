package org.lambda3.indra.entity;

public interface Extensible {

    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }
}