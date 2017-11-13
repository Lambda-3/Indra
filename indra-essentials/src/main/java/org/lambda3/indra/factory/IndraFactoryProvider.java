package org.lambda3.indra.factory;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

import org.lambda3.indra.factory.IndraFactory;

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
