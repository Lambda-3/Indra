package org.lambda3.indra.rest.resources;

import org.lambda3.indra.common.client.NGramToken;

import java.util.List;

public class NGramsRequest {

    public List<NGramToken> tokens;
    public List<Integer> positions;
}
