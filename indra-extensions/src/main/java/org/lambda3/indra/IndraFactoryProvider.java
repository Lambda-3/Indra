package org.lambda3.indra;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class IndraFactoryProvider {

    private ServiceLoader<IndraFactory> loader;

    public IndraFactoryProvider() {
        loader = ServiceLoader.load(IndraFactory.class);
        validate();
    }

    private void validate() {
        Map<Class, Map<String, String>> names = new HashMap<>();
        for (IndraFactory factory : loader) {
            Class clazz = factory.getServingClass();
            Map<String, String> content = names.getOrDefault(clazz, new HashMap<>());
            if (!names.containsKey(clazz)) {
                names.put(clazz, content);
            }

            String name = factory.getName().toLowerCase();
            if (!content.containsKey(name)) {
                content.put(name, factory.getClass().getCanonicalName());

            } else {
                throw new Error(String.format("There are more than one factory of %s with the same name (%s). Classes are %s and %s",
                        getClass().getSimpleName(), name, names.get(name), factory.getClass().getCanonicalName()));
            }
        }

        for (Map<String, String> content : names.values()) {
            for (String name : content.keySet()) {
                if (name.contains(IndraFactory.FACTORY_SEPARATOR) || name.contains(IndraFactory.PARAMS_SEPARATOR)) {
                    throw new Error(String.format("'getName' of factories can't contain the characters %s or %s. Error in %s |getName=%s.",
                            IndraFactory.FACTORY_SEPARATOR, IndraFactory.PARAMS_SEPARATOR, names.get(name), name));
                }
            }
        }
    }

    public <T> IndraFactory<T> get(String name, Class<T> servingClass) {
        for (IndraFactory f : loader) {
            if (f.getServingClass().equals(servingClass) && f.getName().equalsIgnoreCase(name)) {
                return f;
            }
        }

        return null;
    }
}
