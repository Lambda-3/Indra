package org.lambda3.indra.rest.resources;

import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.Model;
import org.lambda3.indra.common.client.ScoreFunction;
import org.lambda3.indra.common.client.TextPair;

import java.util.List;

public final class RelatednessRequest {

    public String corpus;
    public Model model;
    public Language language;
    public List<TextPair> pairs;
    public ScoreFunction scoreFunction;

    @Override
    public String toString() {
        return "RelatednessRequest{" +
                "corpus='" + corpus + '\'' +
                ", model=" + model +
                ", language=" + language +
                ", pairs=" + pairs +
                ", scoreFunction=" + scoreFunction +
                '}';
    }
}
