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
