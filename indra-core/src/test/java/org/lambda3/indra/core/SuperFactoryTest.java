package org.lambda3.indra.core;

import org.lambda3.indra.entity.relatedness.CosineRelatednessFunction;
import org.lambda3.indra.entity.relatedness.RelatednessFunction;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SuperFactoryTest {

    @Test
    public void testCreatingScoreFunction() {
        SuperFactory sf = new SuperFactory();
        RelatednessFunction func = sf.create("cosine", RelatednessFunction.class);
        Assert.assertTrue(func != null);
        Assert.assertEquals(CosineRelatednessFunction.class, func.getClass());
    }
}
