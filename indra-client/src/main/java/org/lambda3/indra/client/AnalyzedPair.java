package org.lambda3.indra.client;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AnalyzedPair {
    private final Map<String, List<String>> analyzed = new ConcurrentHashMap<>();
    private final TextPair pair;

    public AnalyzedPair(TextPair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("pair can't be null");
        }
        this.pair = pair;
    }

    public void add(String text, List<String> tokens) {
        if (text.equals(pair.t1) || text.equals(pair.t2)) {
            analyzed.put(text, tokens);
        }
        else {
            throw new RuntimeException("{} not found in the underlying TextPair.");
        }
    }

    public TextPair getTextPair() {
        return this.pair;
    }

    public List<String> getT1() {
        return analyzed.get(pair.t1);
    }

    public List<String> getT2() {
        return analyzed.get(pair.t2);
    }
}
