package org.lambda3.indra.annoy;

import org.testng.annotations.Test;

public class AnnoyVectorSpaceTest {

    private static final String BASE_MODEL = "";

    @Test(enabled = false)
    public void creatingModel() {
        //TODO incomplete
        String localModel = getClass().getClassLoader().getResource(BASE_MODEL).getPath();
        AnnoyVectorSpace vs = new AnnoyVectorSpace(localModel);
    }
}
