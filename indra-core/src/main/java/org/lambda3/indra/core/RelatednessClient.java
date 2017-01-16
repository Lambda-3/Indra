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

import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.ScoredTextPair;
import org.lambda3.indra.common.client.TextPair;
import org.lambda3.indra.core.exception.RelatednessError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class RelatednessClient {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract List<ScoredTextPair> compute(List<AnalyzedPair> pairs);

    protected abstract Params getParams();

    private AnalyzedPair doAnalyze(IndraAnalyzer analyzer, TextPair p) {
        try {
            return analyzer.analyze(p);
        } catch (IOException e) {
            logger.error("Error analyzing {}", p, e);
        }
        return null;
    }

    private List<ScoredTextPair> doCompute(List<TextPair> pairs)  {
        logger.debug("Analyzing {} pairs", pairs.size());
        try {
            IndraAnalyzer analyzer = new IndraAnalyzer(getParams().language, getParams().useStemming());
            List<AnalyzedPair> analyzedPairs = new ArrayList<>();
            pairs.forEach(p -> {
                AnalyzedPair analyzedPair = doAnalyze(analyzer, p);
                if (analyzedPair != null)
                    analyzedPairs.add(doAnalyze(analyzer, p));
            });

            logger.debug("Computing relatedness..");
            List<ScoredTextPair> r = compute(analyzedPairs);
            logger.debug("Done.");
            return r;
        }
        catch(Exception e) {
            throw new RelatednessError(e);
        }
    }

    public final RelatednessResult getRelatedness(List<TextPair> pairs) {
        return new RelatednessResult(doCompute(pairs));
    }
}