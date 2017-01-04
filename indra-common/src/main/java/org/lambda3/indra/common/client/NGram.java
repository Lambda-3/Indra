package org.lambda3.indra.common.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NGram {

    public List<Arc> arcs;

    public long quantity;

    public NGram(int size) {
        this.arcs = new ArrayList<>(size);
    }

    @Override
    public String toString() {
        return String.format("[%s] %d-gram - quantity: %d | arcs %s",
                getClass().getSimpleName(), arcs.size(), quantity, Arrays.toString(arcs.toArray()));
    }
}
