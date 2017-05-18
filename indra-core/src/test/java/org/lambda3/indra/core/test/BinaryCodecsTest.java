package org.lambda3.indra.core.test;

import org.apache.commons.math3.linear.RealVector;
import org.lambda3.indra.core.codecs.BinaryCodecs;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public final class BinaryCodecsTest {

    @Test
    public void denseSerializationTest() throws IOException {
        double[] v1 = new double[] {1.52, 1/3, -3.1};
        byte[] b = BinaryCodecs.marshall(v1);
        RealVector realVector = BinaryCodecs.unmarshall(b, false, 3);
        Assert.assertEquals(v1, realVector.toArray());
    }

}
