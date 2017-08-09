package org.lambda3.indra.core.threshold;

import org.lambda3.indra.IndraFactoryProvider;
import org.lambda3.indra.Threshold;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ThreasholdTest {
/**
    private IndraFactoryProvider<Threshold> provider = new IndraFactoryProvider<>(Threshold.class);
    private Map<String, Double> originalTerms;

    @BeforeTest
    public void addTerms() {
        Map<String, Double> localTerms = new HashMap<>();
        localTerms.put("i1", 1d);
        localTerms.put("i2", .9);
        localTerms.put("i3", .51);
        localTerms.put("i4", .5);
        localTerms.put("i5", .49999999999);
        localTerms.put("i6", .4);
        localTerms.put("i7", .3);
        localTerms.put("i8", .1);
        localTerms.put("i9", .00000000000001);
        localTerms.put("i0", 0d);
        originalTerms = Collections.unmodifiableMap(localTerms);
    }

    @Test
    public void max05Test() {
        LinkedHashMap<String, Double> terms = new LinkedHashMap<>(originalTerms);
        Threshold threshold = provider.get("max-0.5");
        threshold.apply(terms);

        Assert.assertEquals(7, terms.size());
        Assert.assertFalse(terms.containsKey("i1"));
        Assert.assertFalse(terms.containsKey("i2"));
        Assert.assertFalse(terms.containsKey("i3"));
    }

    @Test
    public void max01Test() {
        LinkedHashMap<String, Double> terms = new LinkedHashMap<>(originalTerms);
        Threshold threshold = provider.get("max-0.1");
        threshold.apply(terms);

        Assert.assertEquals(3, terms.size());
        Assert.assertTrue(terms.containsKey("i8"));
        Assert.assertTrue(terms.containsKey("i9"));
        Assert.assertTrue(terms.containsKey("i0"));
    }

    @Test
    public void sameInstances() {
        Threshold t1 = provider.get("max-0.1");
        Threshold t2 = provider.get("MAX-0.1");
        Assert.assertEquals(t1, t2);

        Threshold t3 = provider.get("max-0.1");
        Threshold t4 = provider.get("max-0.5");
        Assert.assertNotEquals(t3, t4);
    }
    */
}
