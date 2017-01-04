package org.lambda3.indra.common.client;

import com.google.common.base.Preconditions;

public final class ScoredTextPair {
    public String t1;
    public String t2;
    public double score;

    public ScoredTextPair() { } // serializer happy

    public ScoredTextPair(AnalyzedPair analyzedPair, double score) {
        Preconditions.checkNotNull(analyzedPair);
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
