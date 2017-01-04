package org.lambda3.indra.core.tests;

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
