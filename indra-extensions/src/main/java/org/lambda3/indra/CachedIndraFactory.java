package org.lambda3.indra;

import org.lambda3.indra.exception.IndraInvalidParameterException;
import org.lambda3.indra.exception.IndraRuntimeException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class CachedIndraFactory<T> implements IndraFactory<T> {

    protected Map<String, T> objects = new HashMap<>();
    protected Class<T> clazz;

    public CachedIndraFactory(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T get(String type) {
        if (!objects.containsKey(type)) {
            create(type);
        }

        return objects.get(type);
    }

    private synchronized void create(String name) throws IndraRuntimeException {
        String[] parts = name.split(PARAMS_SEPARATOR);
        String[] params = parts.length > 1 ? Arrays.copyOfRange(parts, 1, parts.length) : null;

        try {
            T t = create(parts[0], params);
            if (t != null) {
                objects.put(name, t);
            }
        } catch (IndraInvalidParameterException e) {
            throw new IndraRuntimeException(String.format("invalid "));
        }
    }

    @Override
    public Class<T> getServingClass() {
        return clazz;
    }

    protected abstract T create(String name, String... params) throws IndraInvalidParameterException;
}
