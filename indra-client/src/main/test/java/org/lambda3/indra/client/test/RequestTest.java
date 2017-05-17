package org.lambda3.indra.client.test;

import org.lambda3.indra.client.ClientError;
import org.lambda3.indra.client.RelatednessRequest;
import org.lambda3.indra.client.ScoreFunction;
import org.lambda3.indra.client.TextPair;
import org.testng.annotations.Test;

import java.util.Collections;

public final class RequestTest {

    @Test(expectedExceptions = ClientError.class)
    public void clientError1Test() {
        new RelatednessRequest().validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError2Test() {
        new RelatednessRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction(ScoreFunction.COSINE)
                .validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError3Test() {
        new RelatednessRequest()
                .language("en")
                .model("w2v")
                .scoreFunction(ScoreFunction.COSINE)
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError4Test() {
        new RelatednessRequest()
                .corpus("any")
                .model("w2v")
                .scoreFunction(ScoreFunction.COSINE)
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError5Test() {
        new RelatednessRequest()
                .corpus("any")
                .language("en")
                .scoreFunction(ScoreFunction.COSINE)
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError6Test() {
        new RelatednessRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }

    @Test(expectedExceptions = ClientError.class)
    public void clientError7Test() {
        new RelatednessRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .validate();
    }

    @Test
    public void clientRequestWitouhtErrorTest() {
        new RelatednessRequest()
                .corpus("any")
                .language("en")
                .model("w2v")
                .scoreFunction(ScoreFunction.COSINE)
                .pairs(Collections.singletonList(new TextPair("a", "b")))
                .validate();
    }
}
