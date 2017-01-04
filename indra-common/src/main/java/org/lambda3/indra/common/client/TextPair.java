package org.lambda3.indra.common.client;

public final class TextPair {
    public String t1;
    public String t2;

    public TextPair() { /** To make serialization happy */ }

    public TextPair(String t1, String t2) {
        this.t1 = t1;
        this.t2 = t2;
    }


    @Override
    public String toString() {
        return "TermPair{" +
                "t1='" + t1 + '\'' +
                ", t2='" + t2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TextPair)) return false;

        TextPair textPair = (TextPair) o;

        if (t1 != null ? !t1.equals(textPair.t1) : textPair.t1 != null) return false;
        return !(t2 != null ? !t2.equals(textPair.t2) : textPair.t2 != null);

    }

    @Override
    public int hashCode() {
        int result = t1 != null ? t1.hashCode() : 0;
        result = 31 * result + (t2 != null ? t2.hashCode() : 0);
        return result;
    }
}
