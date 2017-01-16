package org.lambda3.indra.common.client;

/*-
 * ==========================License-Start=============================
 * Indra Common Module
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
