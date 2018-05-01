package org.lambda3.indra.util;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class VectorIteratorTest {

    public static float round(float value) {
        final int places = 6;

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    private Map<String, float[]> bases = new HashMap<>();

    @BeforeTest
    public void setBases() {
        bases.put("Academy", new float[]{0.007773047f, -0.043646008f, -0.015704820f, 0.025632130f, 0.002562603f, 0.035509266f, -0.000674200f, 0.014072117f, 0.009514473f, 0.026054339f});
        bases.put("representative", new float[]{0.037685659f, 0.046354908f, 0.012439170f, 0.022717079f, -0.035462331f, 0.043896273f, -0.044595852f, -0.004256723f, 0.030500248f, 0.025464116f});
        bases.put("the", new float[]{-0.047595479f, -0.034353126f, 0.021126181f, -0.043014500f, 0.037415951f, -0.029718515f, -0.029173711f, -0.028166214f, -0.048020538f, -0.049493261f});
    }

    @Test
    public void gensimFormatTest() {
        String gensimModel = getClass().getClassLoader().getResource("models/gensim.bin").getFile();
        try {
            VectorIterator iterator = new VectorIterator(new File(gensimModel), 10, false);

            int count = 0;
            Set<String> cases = new HashSet<>();

            while (iterator.hasNext()) {
                count++;
                TermVector vector = iterator.next();

                if (bases.containsKey(vector.term)) {
                    cases.add(vector.term);

                    float[] base = bases.get(vector.term);
                    for (int b = 0; b < base.length; b++) {
                        Assert.assertEquals(round((float) vector.content.toArray()[b]), round(base[b]));
                    }
                }
            }

            Assert.assertEquals(cases.size(), 3);
            Assert.assertEquals(count, 301);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }


    }
}
