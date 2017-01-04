package org.indra_project.core.tests;

import org.indra_project.core.utils.VectorsUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsTest {

    @Test
    public void addVectorsTest() {
        Map<Integer, Double> v1 = new HashMap<Integer, Double>() {{
            put(1, 0.1);
            put(2, 0.1);
            put(3, 0.1);
            put(6, 0.1);
            put(10, 0.1);
        }};

        Map<Integer, Double> v2 = new HashMap<Integer, Double>() {{
            put(1, 0.1);
            put(2, 0.1);
            put(3, 0.1);
            put(6, 0.1);
            put(12, 0.1);
        }};

        Map<Integer, Double> expected = new HashMap<Integer, Double>() {{
            put(1, 0.2);
            put(2, 0.2);
            put(3, 0.2);
            put(6, 0.2);
            put(10, 0.1);
            put(12, 0.1);
        }};

        List<Map<Integer, Double>> vectors = new ArrayList<Map<Integer, Double>>() {{
            add(v1);
            add(v2);
        }};

        Assert.assertEquals(expected, VectorsUtils.add(vectors));
    }

}
