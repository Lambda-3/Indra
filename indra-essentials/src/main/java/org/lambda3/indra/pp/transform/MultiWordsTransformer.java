package org.lambda3.indra.pp.transform;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MultiWordsTransformer implements Transformer {

    public static final String TOKEN_SEPARATOR = "_";
    private static final String SPACE = " ";
    public final Map<String, List<Integer>> multiWordTokens;

    public MultiWordsTransformer(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        List<String> lines = new LinkedList<>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        this.multiWordTokens = processMultiWordTokens(lines);
    }

    public MultiWordsTransformer(Collection<String> multiWordTokens) {
        List<String> newList = new LinkedList<>(multiWordTokens);
        this.multiWordTokens = processMultiWordTokens(newList);

    }

    private Map<String, List<Integer>> processMultiWordTokens(List<String> multiWordTokens) {
        multiWordTokens.sort((s1, s2) -> s2.length() - s1.length());

        Map<String, List<Integer>> map = new LinkedHashMap<>();
        for (String s : multiWordTokens) {
            s = s.trim();
            if (s.length() == 0) {
                continue;
            }

            map.put(s, new LinkedList<>());
            List<Integer> spaces = map.get(s);

            int pos = -1;
            do {
                pos++;
                pos = s.indexOf(SPACE, pos);
                if (pos > 0) {
                    spaces.add(pos);
                } else if (spaces.isEmpty()) {
                    map.remove(s);
                }
            } while (pos >= 0);
        }
        return Collections.unmodifiableMap(map);
    }

    @Override
    public void transform(StringBuilder content) {
        for (String mwt : multiWordTokens.keySet()) {
            List<Integer> spaces = multiWordTokens.get(mwt);

            int start = 0;
            while (start >= 0) {
                start = content.indexOf(mwt, start);
                if (start != -1) {
                    for (int pos : spaces) {
                        content.replace(start + pos, start + pos + 1, TOKEN_SEPARATOR);
                    }
                }
            }

        }
    }
}
