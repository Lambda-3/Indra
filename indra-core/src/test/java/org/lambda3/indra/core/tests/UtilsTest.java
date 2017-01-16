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

import org.lambda3.indra.core.utils.VectorsUtils;
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
