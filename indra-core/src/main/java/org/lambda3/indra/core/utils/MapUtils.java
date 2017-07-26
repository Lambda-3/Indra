package org.lambda3.indra.core.utils;

import java.util.*;

public class MapUtils {

    //TODO instead of Map, all the interface could use List<Entry> to avoid the overhead of sorting transformation.
    //TODO implement a list that order during insertion for this purpose.
    public static LinkedHashMap entriesSortedByValues(Map map) {
        List<Map.Entry> sortedEntries = new ArrayList<>(map.entrySet());
        Collections.sort(sortedEntries, (e1, e2) -> ((Comparable) e2.getValue()).compareTo(e1.getValue()));

        LinkedHashMap output = new LinkedHashMap();
        sortedEntries.forEach(entry -> output.put(entry.getKey(), entry.getValue()));

        return output;
    }
}
