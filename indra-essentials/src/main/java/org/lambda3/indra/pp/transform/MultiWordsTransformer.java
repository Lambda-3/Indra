package org.lambda3.indra.pp.transform;

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
