package org.indra_project.rest.resources;

import org.indra_project.common.client.NGramToken;

import java.util.List;

public class NGramsRequest {

    public List<NGramToken> tokens;
    public List<Integer> positions;
}
