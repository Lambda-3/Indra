package org.lambda3.indra.core.test;

/*-
 * ==========================License-Start=============================
 * Indra Mongo Module
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

import org.lambda3.indra.core.translation.IndraTranslator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndraTranslatorTest {

    @Test
    public void getRelevantTranslationsTest() {
        Map<String, Double> tr = new HashMap<>();
        tr.put("work", 1d);
        tr.put("love", 10d);
        tr.put("hate", 0d);
        tr.put("mother", 6d);
        tr.put("child", 7d);
        tr.put("wife", 8d);
        tr.put("science", 4d);

        List<String> res = IndraTranslator.getRelevantTranslations(tr);
        Assert.assertEquals(res.size(), 5);
        Assert.assertTrue(res.contains("love"));
        Assert.assertTrue(res.contains("mother"));
        Assert.assertTrue(res.contains("child"));
        Assert.assertTrue(res.contains("wife"));
        Assert.assertTrue(res.contains("science"));
    }
}
