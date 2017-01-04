package org.indra_project.common.client;

import java.util.Comparator;

public class Arc implements Comparator<Arc> {

    public NGramToken token;

    //position of the token in the ngram
    public int position;

    //the origin of the arc. The destination is the token in this class.
    public int head;

    //label of the arc.
    public String label;

    public Arc() {

    }

    public Arc(NGramToken token, int postion, int head, String label) {
        this.token = token;
        this.position = postion;
        this.head = head;
        this.label = label;
    }

    @Override
    public int compare(Arc first, Arc second) {
        return first.position - second.position;
    }

    @Override
    public String toString() {
        return String.format("[%s] - pos: %d | head: %d | label: %s || %s",
                getClass().getSimpleName(), this.position, this.head, this.label, this.token.toString());
    }
}
