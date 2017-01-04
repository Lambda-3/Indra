package org.lambda3.indra.rest.resources;

import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.Model;
import org.lambda3.indra.common.client.ScoreFunction;
import org.lambda3.indra.common.client.ScoredTextPair;

import java.util.Collection;

public final class RelatednessResponse {
    public String corpus;
    public Model model;
    public Language language;
    public Collection<ScoredTextPair> pairs;
    public ScoreFunction scoreFunction;

    @Override
    public String toString() {
        return "RelatednessResponse{" +
                "corpus='" + corpus + '\'' +
                ", model=" + model +
                ", language=" + language +
                ", pairs=" + pairs +
                ", scoreFunction=" + scoreFunction +
                '}';
    }
}
