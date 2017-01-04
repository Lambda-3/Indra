package org.indra_project.rest.resources;

import org.indra_project.common.client.Language;
import org.indra_project.common.client.Model;
import org.indra_project.common.client.ScoreFunction;
import org.indra_project.common.client.TextPair;

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
