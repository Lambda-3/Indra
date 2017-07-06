package org.lambda3.indra.client;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

import java.util.List;
import java.util.Objects;

public class NeighborRelatednessRequest extends RelatednessRequest<NeighborRelatednessRequest> {
    private List<String> terms;
    private int topk;

    public NeighborRelatednessRequest terms(List<String> terms) {
        this.terms = Objects.requireNonNull(terms);
        return this;
    }

    public List<String> getTerms() {
        return terms;
    }

    public NeighborRelatednessRequest topk(int topk) {
        this.topk = topk;
        return this;
    }

    public int getTopk() {
        return topk;
    }

    @Override
    protected String isValid() {
        String errors = super.isValid();
        StringBuilder errorMessages = new StringBuilder(errors);
        checkAndAppendErrorMessagesLists(terms, "terms", errorMessages);

        if(topk <= 0) {
            errorMessages.append(" - 'topk' must be greater than 0;\n");
        }

        if(isMt()) {
            errorMessages.append(" - machine translation not supported in this function. 'mt' can't be true;\n");
        }

        return errorMessages.toString();
    }

}
