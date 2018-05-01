package org.lambda3.indra.core.test;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

import org.lambda3.indra.ScoredTextPair;
import org.lambda3.indra.TextPair;
import org.lambda3.indra.core.SuperFactory;
import org.lambda3.indra.composition.VectorComposer;
import org.lambda3.indra.relatedness.RelatednessFunction;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

public class RelatednessTest {

    @Test
    public void relatednessSimpleTest() {
        SuperFactory sf = new SuperFactory();
        RelatednessDummyClient cli = new RelatednessDummyClient();
        TextPair pair = new TextPair("car", "engine");

        RelatednessFunction func = sf.create("cosine", RelatednessFunction.class);
        VectorComposer termComposer = sf.create("sum", VectorComposer.class);
        VectorComposer translationComposer = sf.create("average", VectorComposer.class);

        List<ScoredTextPair> res = cli.getRelatedness(Collections.singletonList(pair), func, termComposer, translationComposer);

        Assert.assertNotNull(res);
        Assert.assertEquals(1, res.size());
        ScoredTextPair scoredPair = res.get(0);
        Assert.assertEquals(pair.t1, scoredPair.t1);
        Assert.assertEquals(pair.t2, scoredPair.t2);
    }
}
