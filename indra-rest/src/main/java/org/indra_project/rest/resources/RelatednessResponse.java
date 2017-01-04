package org.indra_project.rest.resources;

import org.indra_project.common.client.Language;
import org.indra_project.common.client.Model;
import org.indra_project.common.client.ScoreFunction;
import org.indra_project.common.client.ScoredTextPair;

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
