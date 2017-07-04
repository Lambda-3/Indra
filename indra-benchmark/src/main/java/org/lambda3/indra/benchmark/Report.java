package org.lambda3.indra.benchmark;

/*-
 * ==========================License-Start=============================
 * Indra Benchmark Module
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

import java.util.LinkedList;
import java.util.List;

public class Report {
    public final String desc;
    private List<String> content = new LinkedList<>();

    public Report(String desc) {
        this.desc = desc;
    }

    public void addInfo(String dataset, String cand1, Double score1, String cand2, Double score2) {

        Double winnerPercent = (score1 < score2 ? score1 / score2 : score2 / score1);
        String winner = (score1 < score2 ? cand1 : cand2);
        String data = String.format("%s - [%f] Winner -> %s | %s=%f | %s=%f", dataset, winnerPercent, winner,
                cand1, score1, cand2, score2);

        content.add(data);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(desc);

        for (String data : content) {
            sb.append("\n    ");
            sb.append(data);
        }

        return sb.toString();
    }
}
