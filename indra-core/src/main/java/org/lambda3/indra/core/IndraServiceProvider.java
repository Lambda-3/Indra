package org.lambda3.indra.core;

import java.util.*;

public class IndraServiceProvider<T extends Servable> {

    protected Map<String, T> objects = new HashMap<>();

    public IndraServiceProvider(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        Iterator<T> items = loader.iterator();
        while (items.hasNext()) {
            T item = items.next();
            objects.put(item.getClass().getCanonicalName(), item);
            objects.put(item.getClass().getSimpleName(), item);
        }
    }

    public T get(String type) {
        return objects.get(type);
    }

    public T getAny() {
        return objects.values().iterator().next();
    }

    public Set<String> availableServices() {
        return objects.keySet();
    }

}
