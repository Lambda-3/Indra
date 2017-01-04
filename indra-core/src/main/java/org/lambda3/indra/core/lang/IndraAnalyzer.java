package org.lambda3.indra.core.lang;

import com.google.common.base.Preconditions;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.lambda3.indra.common.client.AnalyzedPair;
import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.TextPair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.ext.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IndraAnalyzer {
    public static final int MIN_WORD_LENGTH = 3;
    public static final int MAX_WORD_LENGTH = 100;

    private static Logger logger = LoggerFactory.getLogger(IndraAnalyzer.class);

    private Tokenizer tokenizer;
    private TokenStream stream;

    public IndraAnalyzer(Language lang, boolean stemming) {
        Preconditions.checkNotNull(lang);
        logger.info("Creating analyzer, lang={} (stemming={})", lang, stemming);
        tokenizer = new StandardTokenizer();
        stream = createStream(lang, stemming, tokenizer);
    }

    public List<String> analyze(String text) throws IOException {
        List<String> result = new ArrayList<>();
        try (StringReader reader = new StringReader(text)) {
            tokenizer.setReader(reader);
            CharTermAttribute cattr = stream.addAttribute(CharTermAttribute.class);
            stream.reset();
            while (stream.incrementToken()) {
                result.add(cattr.toString());
            }
        }
        finally {
            stream.end();
            stream.close();
        }

        return result;
    }

    public AnalyzedPair analyze(TextPair pair) throws IOException {
        AnalyzedPair analyzedPair = new AnalyzedPair(pair);
        analyzedPair.add(pair.t1, analyze(pair.t1));
        analyzedPair.add(pair.t2, analyze(pair.t2));
        return analyzedPair;
    }

    private TokenStream createStream(Language lang, boolean stemming, Tokenizer tokenizer) {
        TokenStream stream = new StandardFilter(tokenizer);

        StopFilter stopFilterStream = getStopFilter(lang, stream);
        stream = stopFilterStream != null ? stopFilterStream : stream;

        if (lang != Language.ZH && lang != Language.KO)
            stream = new LengthFilter(stream, MIN_WORD_LENGTH, MAX_WORD_LENGTH);

        TokenStream stemmerStream = stemming ? getStemmer(lang, stream) : null;
        stream = stemmerStream != null ? stemmerStream : stream;

        stream = new LowerCaseFilter(stream);
        return new ASCIIFoldingFilter(stream);
    }

    private SnowballFilter getStemmer(Language lang, TokenStream stream) {
        switch (lang) {
            case EN:
                return new SnowballFilter(stream, new EnglishStemmer());
            case PT:
                return new SnowballFilter(stream, new PortugueseStemmer());
            case ES:
                return new SnowballFilter(stream, new SpanishStemmer());
            case DE:
                return new SnowballFilter(stream, new GermanStemmer());
            case FR:
                return new SnowballFilter(stream, new FrenchStemmer());
            case SV:
                return new SnowballFilter(stream, new SwedishStemmer());
            case IT:
                return new SnowballFilter(stream, new ItalianStemmer());
            case NL:
                return new SnowballFilter(stream, new DutchStemmer());
            case RU:
                return new SnowballFilter(stream, new RussianStemmer());

            /**
             * TODO
             */
            case AR:
            case FA:
            case ZH:
            case KO:
                throw new RuntimeException("Language not implemented yet!");
            default:
                throw new RuntimeException("Language not supported yet!");
        }
    }

    private StopFilter getStopFilter(Language lang, TokenStream stream) {
        try {
            InputStream in = ClassLoader.getSystemResourceAsStream(lang.toString().toLowerCase() + ".stopwords");
            if (in != null) {
                logger.debug("Loading Stop words for lang={}", lang);
                CharArraySet stopWords = new CharArraySet(30, true);
                try (BufferedReader bin = new BufferedReader(new InputStreamReader(in))) {
                    String line;
                    String[] parts;
                    while ((line = bin.readLine()) != null) {
                        parts = line.split(Pattern.quote("|"));
                        line = parts[0].trim();

                        if (line.length() > 0) {
                            stopWords.add(line);
                        }
                    }

                }
                return new StopFilter(stream, stopWords);
            }
            else{
                logger.warn("No stop words found for lang={}", lang);
            }
        }
        catch (Exception e) {
            logger.error("Error creating stop filter for lang={}", lang, e);
        }
        return null;
    }
}
