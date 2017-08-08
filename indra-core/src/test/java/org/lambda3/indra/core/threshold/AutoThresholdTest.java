package org.lambda3.indra.core.threshold;

import org.lambda3.indra.core.threshold.AutoThreshold;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

public class AutoThresholdTest {

    @Test
    public void test() {
        AutoThreshold filter = new AutoThreshold();
        LinkedHashMap<String, Double> values = new LinkedHashMap<>();
        values.put("love", 1d);
        values.put("always", .9);
        values.put("never", .8);
        values.put("good", .6);
        values.put("hate", .5);
        values.put("cell", .1);
        values.put("phone", 0d);

        filter.apply(values);
        Assert.assertEquals(values.size(), 5);
        Assert.assertTrue(values.containsKey("love"));
        Assert.assertTrue(values.containsKey("hate"));
        Assert.assertFalse(values.containsKey("cell"));
        Assert.assertFalse(values.containsKey("phone"));
    }
}
