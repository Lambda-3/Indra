package org.lambda3.indra.core;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

import org.lambda3.indra.common.client.ScoredTextPair;
import org.lambda3.indra.common.client.TextPair;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class RelatednessResult {
    private Map<TextPair, ScoredTextPair> results;

    public RelatednessResult(Collection<ScoredTextPair> scoredTerms) {
        if (scoredTerms == null) {
            throw new IllegalArgumentException("scoreTerms can't be null");
        }
        results = new LinkedHashMap<>();
        for (ScoredTextPair scoredTextPair : scoredTerms) {
            results.put(new TextPair(scoredTextPair.t1, scoredTextPair.t2), scoredTextPair);
        }
    }

    public ScoredTextPair getScore(TextPair pair) {
        return results.get(pair);
    }

    public Collection<ScoredTextPair> getScores() {
        return results.values();
    }
}
