package org.lambda3.indra.client;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
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

public final class ScoredTextPair {
    public String t1;
    public String t2;
    public double score;

    public ScoredTextPair() { } // serializer happy

    public ScoredTextPair(AnalyzedPair analyzedPair, double score) {
        if (analyzedPair == null) {
            throw new IllegalArgumentException("analyzedPair can't be null");
        }
        this.t1 = analyzedPair.getTextPair().t1;
        this.t2 = analyzedPair.getTextPair().t2;
        this.score = score;
    }

    @Override
    public String toString() {
        return "ScoredTermPair{" +
                "t1='" + t1 + '\'' +
                ", t2='" + t2 + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScoredTextPair)) return false;

        ScoredTextPair termPair = (ScoredTextPair) o;

        if (Double.compare(termPair.score, score) != 0) return false;
        if (t1 != null ? !t1.equals(termPair.t1) : termPair.t1 != null) return false;
        return !(t2 != null ? !t2.equals(termPair.t2) : termPair.t2 != null);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = t1 != null ? t1.hashCode() : 0;
        result = 31 * result + (t2 != null ? t2.hashCode() : 0);
        temp = Double.doubleToLongBits(score);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
