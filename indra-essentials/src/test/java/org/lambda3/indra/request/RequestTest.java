package org.lambda3.indra.request;

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
