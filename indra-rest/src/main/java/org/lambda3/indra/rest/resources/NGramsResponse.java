package org.lambda3.indra.rest.resources;

import org.lambda3.indra.common.client.NGram;

import java.util.Collection;
import java.util.Collections;

public class NGramsResponse {

    public static final NGramsResponse EMPTY_RESPONSE = new NGramsResponse(Collections.EMPTY_LIST);

    public Collection<NGram> nGrams;

    public NGramsResponse(Collection<NGram> nGrams) {
        this.nGrams = nGrams;
    }
}
