package org.lambda3.indra.request;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
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

import org.lambda3.indra.TextPair;
import org.lambda3.indra.exception.InvalidRequestException;
import org.lambda3.indra.request.RelatednessPairRequest;
import org.testng.annotations.Test;

import java.util.Collections;

public final class RequestTest {

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError1Test() throws InvalidRequestException {
        new RelatednessPairRequest().validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError2Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError3Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError4Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError5Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError6Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = InvalidRequestException.class)
    public void clientError7Test() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .validate();
    }

    @Test
    public void clientRequestWitouhtErrorTest() throws InvalidRequestException {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }
}
