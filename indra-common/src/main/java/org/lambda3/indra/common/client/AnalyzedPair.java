package org.lambda3.indra.common.client;

/*-
 * ==========================License-Start=============================
 * Indra Common Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
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
