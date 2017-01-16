package org.lambda3.indra.core.tests;

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

import org.lambda3.indra.core.*;
import org.lambda3.indra.common.client.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;

public class RelatednessTest {

    @Test
    public void relatednessSimpleTest() {
        Params params = new Params("corpus", ScoreFunction.COSINE, Language.EN, Model.ESA);
        RelatednessDummyClient cli = new RelatednessDummyClient(params);
        TextPair pair = new TextPair("car", "engine");

        RelatednessResult res = cli.getRelatedness(Collections.singletonList(pair));

        Assert.assertNotNull(res);
        Assert.assertEquals(1, res.getScores().size());
        ScoredTextPair scoredPair = res.getScore(pair);
        Assert.assertEquals(pair.t1, scoredPair.t1);
        Assert.assertEquals(pair.t2, scoredPair.t2);
    }
}
