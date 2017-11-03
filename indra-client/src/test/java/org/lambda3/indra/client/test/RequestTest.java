package org.lambda3.indra.client.test;

import org.lambda3.indra.client.RelatednessPairRequest;
import org.lambda3.indra.client.TextPair;
import org.testng.annotations.Test;

import javax.ws.rs.BadRequestException;
import java.util.Collections;

public final class RequestTest {

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError1Test() {
        new RelatednessPairRequest().validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError2Test() {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError3Test() {
        new RelatednessPairRequest()
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError4Test() {
        new RelatednessPairRequest()
                .corpus("any")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError5Test() {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError6Test() {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = BadRequestException.class)
    public void clientError7Test() {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .validate();
    }

    @Test
    public void clientRequestWitouhtErrorTest() {
        new RelatednessPairRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction("cosine")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }
}
