package org.indra_project.rest.resources;

import org.indra_project.common.client.NGram;

import java.util.Collection;
import java.util.Collections;

public class NGramsResponse {

    public static final NGramsResponse EMPTY_RESPONSE = new NGramsResponse(Collections.EMPTY_LIST);

    public Collection<NGram> nGrams;

    public NGramsResponse(Collection<NGram> nGrams) {
        this.nGrams = nGrams;
    }
}
