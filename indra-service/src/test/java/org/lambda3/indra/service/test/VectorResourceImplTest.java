package org.lambda3.indra.service.test;

import org.lambda3.indra.client.*;
import org.lambda3.indra.core.IndraDriver;
import org.lambda3.indra.core.VectorSpace;
import org.lambda3.indra.core.VectorSpaceFactory;
import org.lambda3.indra.core.composition.VectorComposerFactory;
import org.lambda3.indra.core.test.MockCachedVectorSpace;
import org.lambda3.indra.core.test.MockIndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.IndraTranslatorFactory;
import org.lambda3.indra.service.impl.VectorResourceImpl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class VectorResourceImplTest {

    @Test
    public void generalTest() {
        VectorSpaceFactory vectorSpaceFactory = createVectorSpaceFactor();
        IndraTranslatorFactory translatorFactory = createIndraTranslatorFactory();
        IndraDriver driver = new IndraDriver(vectorSpaceFactory, translatorFactory);
        VectorResourceImpl impl = new VectorResourceImpl(driver);

        List<String> terms = Arrays.asList("love", "mother", "father");
        VectorRequest request = new VectorRequest().language("EN").mt(false).terms(terms);
        VectorResponse response = impl.getVector(request);
        Assert.assertNotNull(response);
        Assert.assertTrue(response instanceof DenseVectorResponse);

        Map<String, double[]> vectors = ((DenseVectorResponse) response).getTerms();
        Assert.assertNotNull(vectors);
        Assert.assertEquals(vectors.size(), 3);

        for (double[] array : vectors.values()) {
            Assert.assertTrue(array.length > 0);
        }

        Assert.assertFalse(response instanceof SparseVectorResponse);
    }


    //TODO repeated code to be solved.
    private static VectorSpaceFactory createVectorSpaceFactor() {
        VectorSpaceFactory factory = new VectorSpaceFactory() {
            @Override
            protected VectorSpace doCreate(AbstractBasicRequest request) {
                VectorComposerFactory composerFactory = new VectorComposerFactory();
                return new MockCachedVectorSpace(composerFactory.getComposer(IndraDriver.DEFAULT_TERM_COMPOSTION),
                        composerFactory.getComposer(IndraDriver.DEFAULT_TRANSLATION_COMPOSTION));
            }

            @Override
            protected String createKey(AbstractBasicRequest request) {
                return request.toString();
            }

            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }
        };

        return factory;
    }

    private static IndraTranslatorFactory createIndraTranslatorFactory() {
        IndraTranslatorFactory factory = new IndraTranslatorFactory() {
            @Override
            public Collection<String> getAvailableModels() {
                return null;
            }

            @Override
            protected IndraTranslator doCreate(AbstractBasicRequest request) {
                return new MockIndraTranslator();
            }

            @Override
            protected String createKey(AbstractBasicRequest request) {
                return request.toString();
            }
        };

        return factory;
    }
}
