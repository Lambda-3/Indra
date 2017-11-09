package org.lambda3.indra.factory;

import org.lambda3.indra.Extensible;

public interface IndraFactory<T> extends Extensible {
    String BUILT_IN_FACTORY = "builtin";
    String FACTORY_SEPARATOR = ":";
    String PARAMS_SEPARATOR = "-";

    T get(String name);

    T getDefault();

    Class<T> getServingClass();

    String getName();

    //TODO List<String> getAvailableInplementations();
}
