package org.lambda3.indra.factory;

import org.lambda3.indra.exception.IndraInvalidParameterException;
import org.lambda3.indra.exception.IndraRuntimeException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class CachedIndraFactory<T> implements IndraFactory<T> {

    protected final Map<String, T> objects = new HashMap<>();
    protected Class<T> clazz;

    public CachedIndraFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get(String name) {
        if (name == null || name.trim().length() == 0) {
            return getDefault();
        }

        String[] parts = name.split(PARAMS_SEPARATOR);
        String[] params = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : null;
        String newName = parts[0].toLowerCase() + PARAMS_SEPARATOR + (params != null ? String.join(PARAMS_SEPARATOR, params) : "");

        if (!objects.containsKey(newName)) {
            synchronized (objects) {
                try {
                    T t = create(parts[0].toLowerCase(), params);
                    if (t != null) {
                        objects.put(newName, t);
                    }
                } catch (IndraInvalidParameterException e) {
                    throw new IndraRuntimeException(String.format("invalid "));
                }
            }
        }

        return objects.get(newName);
    }

    @Override
    public Class<T> getServingClass() {
        return clazz;
    }

    protected abstract T create(String name, String... params) throws IndraInvalidParameterException;
}
