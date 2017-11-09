package org.lambda3.indra.pp;

import org.lambda3.indra.pp.transform.MultiWordsTransformer;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MultiWordsTransformerTest {

    private String text = "price action is the movement of a security's price. " +
            "price action is encompassed in technical and chart pattern analysis, which attempt to find " +
            "order in the sometimes seemingly random movement of price. Swings (high and low), tests of " +
            "resistance and consolidation are some examples of price action.";

    @Test
    public void emptyStringTest() {
        List<String> tokens = Arrays.asList("price action", "");
        new MultiWordsTransformer(tokens).transform(new StringBuilder(text));
    }

    @Test(timeOut = 20000)
    public void foundTokensTest() {
        List<String> tokens = Arrays.asList("price action", "random movement", "high and low");
        StringBuilder content = new StringBuilder(text);
        new MultiWordsTransformer(tokens).transform(content);
        String strContent = content.toString();

        for (String token : tokens) {
            Assert.assertFalse(strContent.contains(token));
            Assert.assertTrue(strContent.contains(token.replace(" ", MultiWordsTransformer.TOKEN_SEPARATOR)));
            Assert.assertEquals(text, strContent.replace(MultiWordsTransformer.TOKEN_SEPARATOR, " "));
            Assert.assertTrue(strContent.contains("are some examples"));
        }
    }

    @Test(timeOut = 20000)
    public void nonExistingTokensTest() {
        List<String> tokens = Arrays.asList("frevo and samba", "financial crisis", "best of luck");
        StringBuilder content = new StringBuilder(text);
        new MultiWordsTransformer(tokens).transform(content);
        Assert.assertEquals(content.toString(), text);
    }

    @Test
    public void multiWordFinanceCorpus() {
        InputStream terms = getClass().getClassLoader().getResourceAsStream("corpora/financeCorpus/finance_terms.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(terms));
        List<String> mwt = reader.lines().collect(Collectors.toList());

        StringBuilder content = new StringBuilder();
        InputStream contentStream = getClass().getClassLoader().getResourceAsStream("corpora/financeCorpus/corpus.txt");
        BufferedReader contentReader = new BufferedReader(new InputStreamReader(contentStream));
        contentReader.lines().forEach(l -> {
            content.append(l);
            content.append(" ");
        });

        StringBuilder newContent = new StringBuilder(content.toString());
        new MultiWordsTransformer(mwt).transform(newContent);
        Assert.assertTrue(newContent.toString().contains("Photographer: Herman Avakian"));
        Assert.assertEquals(content.toString().replace("_", " "),
                newContent.toString().replace("_", " "));
    }

}
