package org.lambda3.indra;

public interface IndraFactory<T> extends Extensible {
    String BUILT_IN_FACTORY = "built-in";
    String FACTORY_SEPARATOR = ":";
    String PARAMS_SEPARATOR = "-";

    T get(String name);

    Class<T> getServingClass();
}
