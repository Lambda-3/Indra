package org.lambda3.indra.core;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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
import org.lambda3.indra.factory.IndraFactoryProvider;

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
        IndraFactory<T> factory;

        if (fullName != null) {
            String[] parts = fullName.split(IndraFactory.FACTORY_SEPARATOR);
            String item;
            if (parts.length > 1) {
                factory = getFactory(parts[0], clazz);
                item = fullName.substring(parts.length, fullName.length());
            } else {
                factory = getFactory(IndraFactory.BUILT_IN_FACTORY, clazz);
                item = fullName;
            }

            return factory != null ? factory.get(item) : null;
        } else {
            factory = getFactory(IndraFactory.BUILT_IN_FACTORY, clazz);
            return factory != null ? factory.getDefault() : null;
        }

    }
}
