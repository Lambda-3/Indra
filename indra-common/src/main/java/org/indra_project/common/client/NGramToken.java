package org.indra_project.common.client;

public class NGramToken {

    public String word = null;

    public String partOfSpeech = null;

    public NGramToken(String word, String partOfSpeech) {
        this.word = word;
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public String toString() {
        return String.format("[%s] - partOfSpeech: %s | word: %s",
                getClass().getSimpleName(), this.partOfSpeech, this.word);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NGramToken that = (NGramToken) o;

        if (partOfSpeech != null ? !partOfSpeech.equals(that.partOfSpeech) : that.partOfSpeech != null) return false;
        return word != null ? word.equals(that.word) : that.word == null;

    }

    @Override
    public int hashCode() {
        int result = partOfSpeech != null ? partOfSpeech.hashCode() : 0;
        result = 31 * result + (word != null ? word.hashCode() : 0);
        return result;
    }
}
