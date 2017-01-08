package org.lambda3.indra.common.client;

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
