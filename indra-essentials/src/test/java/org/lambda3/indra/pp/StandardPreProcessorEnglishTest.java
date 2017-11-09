package org.lambda3.indra.pp;

import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class StandardPreProcessorEnglishTest {

    private String content = "Eduardo Jorge Martins Alves Sobrinho (born October 26, 1949),[1] most known simply" +
            " as Eduardo Jorge, is a Brazilian public health physician and politician. He is most known for creating " +
            "(or co-creating) the federal laws on family planning, voluntary sterilization, production of generic " +
            "drugs, regulation of asbestos use, and linking budgetary resources for Sistema Único de Saúde.[2] " +
            "300,00 and 32.55";

    @Test
    public void lowercaseAndAccentsTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                applyLowercase(true).removeAccents(true).applyStemmer(0).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        Iterable<String> iterable = () -> pp.process(content);
        String processedContent = String.join(" ", iterable);

        Assert.assertFalse(processedContent.contains("Eduardo Jorge Martins Alves Sobrinho"));
        Assert.assertTrue(processedContent.contains("eduardo jorge martins alves sobrinho"));

        Assert.assertFalse(processedContent.contains("Único"));
        Assert.assertTrue(processedContent.contains("unico"));
    }

    private String transformerTest(String text, List<String> mwt) {
        Map<String, Collection<String>> transformers = Collections.singletonMap("MultiWordsTransformer", mwt);

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                applyLowercase(true).removeAccents(true).applyStemmer(0).transformers(transformers).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        return PreProcessorUtil.toString(pp.process(text));
    }


    @Test
    public void firstTransformerTest() {
        List<String> mwt = Arrays.asList("eduardo jorge martins alves sobrinho", "family planning",
                "sistema único de saúde");

        String processedContent = transformerTest(content, mwt);

        Assert.assertFalse(processedContent.contains("eduardo jorge martins alves sobrinho"));
        Assert.assertTrue(processedContent.contains("eduardo_jorge_martins_alves_sobrinho"));

        Assert.assertFalse(processedContent.contains("family planning"));
        Assert.assertTrue(processedContent.contains("family_planning"));

        Assert.assertFalse(processedContent.contains("sistema único de saúde"));
        Assert.assertFalse(processedContent.contains("sistema_único_de_saúde"));
        Assert.assertTrue(processedContent.contains("sistema_unico_de_saude"));

    }

    @Test
    public void secondTransformerTest() {
        String text = "A simplified version of the 1040A form for individual income tax.";
        List<String> mwt = Arrays.asList("1040a form", "income tax");

        String processedContent = transformerTest(text, mwt);
        Assert.assertFalse(processedContent.contains("1040a form"));
        Assert.assertTrue(processedContent.contains("1040a_form"));

        Assert.assertFalse(processedContent.contains("income tax"));
        Assert.assertTrue(processedContent.contains("income_tax"));

        Assert.assertEquals(text.toLowerCase(), processedContent.replace("_", " ") + ".");
    }

    @Test(timeOut = 10000, enabled = false)
    public void thirdTransformerTest() {
        String text = "What is 10-k? maybe it is a 10-year treasury note. Or it is 125% loan or 130/30 mutual fund" +
                " or even a 1%/10 net 30.";
        List<String> mwt = Arrays.asList("10-k", "10-year treasury note", "125% loan",
                "130/30 mutual fund", "1%/10 net 30");

        String processedContent = transformerTest(text, mwt);
        Assert.assertFalse(processedContent.contains("1040 form"));
        Assert.assertTrue(processedContent.contains("1040_form"));

        Assert.assertFalse(processedContent.contains("income tax"));
        Assert.assertTrue(processedContent.contains("income_tax"));

        Assert.assertEquals(text.toLowerCase(), processedContent.replace("_", " ") + ".");
    }


    @Test
    public void stopWordsTest() {
        Set<String> stopWords = new HashSet<>(Arrays.asList("known", "voluntary", "jorge"));

        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                applyLowercase(true).removeAccents(true).applyStemmer(0).stopWords(stopWords).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertTrue(processedContent.startsWith("eduardo"));
        Assert.assertFalse(processedContent.startsWith("eduardo jorge"));
        Assert.assertTrue(processedContent.startsWith("eduardo martins alves"));

        Assert.assertFalse(processedContent.contains("voluntary"));
        Assert.assertFalse(processedContent.contains("known"));
    }

    @Test
    public void keepNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                replaceNumbers(false).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertTrue(processedContent.contains("26"));
        Assert.assertTrue(processedContent.contains("1949"));
        Assert.assertTrue(processedContent.contains("1"));
        Assert.assertTrue(processedContent.contains("2"));
        Assert.assertFalse(processedContent.contains("<NUMBER>"));

    }

    @Test
    public void removeNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                replaceNumbers(true).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertFalse(processedContent.contains("26"));
        Assert.assertFalse(processedContent.contains("1949"));
        Assert.assertFalse(processedContent.contains("1"));
        Assert.assertFalse(processedContent.contains("2"));
        Assert.assertTrue(processedContent.contains("<NUMBER>"));
    }

    @Test
    public void removeFloatNumbersTest() {
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                replaceNumbers(true).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process("How money is written in English, $30.50 or $30,50?"));
        Assert.assertEquals(processedContent, "how money is written in english <NUMBER> or <NUMBER>");
    }

    @Test
    public void keepAlphaNumTokensTest() {
        String content = "Bla bla bla3. New bla[2], and bla bla[4].";
        CorpusMetadata metadata = CorpusMetadataBuilder.newCorpusMetadata("corpus-name", "en").
                replaceNumbers(true).build();

        StandardPreProcessor pp = new StandardPreProcessor(metadata);
        String processedContent = PreProcessorUtil.toString(pp.process(content));

        Assert.assertTrue(processedContent.contains("bla3"));
        Assert.assertFalse(processedContent.contains("bla 2"));
        Assert.assertFalse(processedContent.contains("bla2"));
        Assert.assertFalse(processedContent.contains("bla 4"));
        Assert.assertFalse(processedContent.contains("bla4"));

    }
}
