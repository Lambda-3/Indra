package org.lambda3.indra.core;

import org.lambda3.indra.IndraFactory;
import org.lambda3.indra.IndraFactoryProvider;

import java.util.HashMap;
import java.util.Map;

public class SuperFactory {

    private IndraFactoryProvider provider = new IndraFactoryProvider();
    private Map<Class, Map<String, IndraFactory>> meta = new HashMap<>();

    private <T> IndraFactory<T> getFactory(String name, Class<T> clazz) {
        Map<String, IndraFactory> fs = meta.getOrDefault(clazz, new HashMap<>());
        if (!meta.containsKey(clazz)) {
            meta.put(clazz, fs);
        }

        IndraFactory factory = fs.getOrDefault(name, provider.get(name, clazz));
        if (!fs.containsKey(name)) {
            fs.put(name, factory);
        }

        return factory;
    }

    public <T> T create(String fullName, Class<T> clazz) {
        String[] parts = fullName.split(IndraFactory.FACTORY_SEPARATOR);
        IndraFactory<T> factory;
        String item;
        if (parts.length > 1) {
            factory = getFactory(parts[0], clazz);
            item = fullName.substring(parts.length, fullName.length());
        } else {
            factory = getFactory(IndraFactory.BUILT_IN_FACTORY, clazz);
            item = fullName;
        }

        return factory != null ? factory.get(item) : null;
    }
}
