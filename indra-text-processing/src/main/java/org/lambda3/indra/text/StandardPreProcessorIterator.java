package org.lambda3.indra.text;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.pattern.PatternReplaceFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.util.CharArraySet;
import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.pp.transform.MultiWordsTransformer;
import org.lambda3.indra.pp.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tartarus.snowball.SnowballProgram;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

class StandardPreProcessorIterator implements Iterator<String> {
    private static Logger logger = LoggerFactory.getLogger(StandardPreProcessorIterator.class);

    private static final String NUMBER_PLACEHOLDER = "<NUMBER>";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+([,\\.][0-9]+)*$");

    private Tokenizer tokenizer;
    private TokenStream tokenStream;
    private CorpusMetadata metadata;
    private CharTermAttribute cattr;
    private String initialPart;
    private List<Transformer> transformers = new LinkedList<>();

    StandardPreProcessorIterator(CorpusMetadata metadata, String text) {
        this.metadata = Objects.requireNonNull(metadata);
        ;
        this.tokenizer = new StandardTokenizer();
        this.tokenStream = createStream(metadata, tokenizer);
        setTransformers();
        initialize(Objects.requireNonNull(text));
    }

    private void setTransformers() {
        for (String trans : metadata.transformers.keySet()) {
            if (trans.equalsIgnoreCase(MultiWordsTransformer.class.getSimpleName())) {
                transformers.add(new MultiWordsTransformer(metadata.transformers.get(trans)));
            } else {
                throw new RuntimeException("Transformer not supported = " + trans);
            }
        }
    }

    private void initialize(String text) {
        this.initialPart = text.substring(0, Math.min(30, text.length()));
        String content = metadata.applyLowercase ? text.toLowerCase() : text;

        if (!transformers.isEmpty()) {
            StringBuilder sbContent = new StringBuilder(content);
            transformers.forEach(t -> t.transform(sbContent));
            content = sbContent.toString();
        }

        StringReader reader = new StringReader(content);

        tokenizer.setReader(reader);
        this.cattr = tokenStream.addAttribute(CharTermAttribute.class);
        try {
            tokenStream.reset();
        } catch (IOException e) {
            logger.error("Error analyzing {}", this.initialPart, e);
        }
    }

    @Override
    public boolean hasNext() {
        try {
            return tokenStream.incrementToken();
        } catch (IOException e) {
            logger.error("Error analyzing {}", this.initialPart, e);
            return false;
        }
    }

    @Override
    public String next() {
        return cattr.toString();
    }

    private TokenStream getStopFilter(String lang, Set<String> metadataStopWords, TokenStream stream) {

        if (metadataStopWords != null && !metadataStopWords.isEmpty()) {
            return new StopFilter(stream, new CharArraySet(metadataStopWords, false));

        } else {
            try {
                InputStream in = ClassLoader.getSystemResourceAsStream(lang.toLowerCase() + ".stopwords");
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
                        return new StopFilter(stream, stopWords);
                    }
                } else {
                    logger.warn("No stop words found for lang={}", lang);
                }
            } catch (Exception e) {
                logger.error("Error creating stop filter for lang={}", lang, e);
            }
        }

        return stream;
    }

    private TokenStream createStream(CorpusMetadata metadata, Tokenizer tokenizer) {
        TokenStream stream = new StandardFilter(tokenizer);
        stream = new LengthFilter(stream, (int) metadata.minTokenLength, (int) metadata.maxTokenLength);

        if (!metadata.stopWords.isEmpty()) {
            stream = getStopFilter(metadata.language, metadata.stopWords, stream);
        }

        if (metadata.applyStemmer > 0) {
            stream = getStemmerFilter(metadata.language, (int) metadata.applyStemmer, stream);
        }

        if (metadata.removeAccents) {
            stream = new ASCIIFoldingFilter(stream);
        }

        if (metadata.replaceNumbers) {
            stream = new PatternReplaceFilter(stream, NUMBER_PATTERN, NUMBER_PLACEHOLDER, false);
        }

        return stream;
    }

    private TokenStream getStemmerFilter(String lang, int times, TokenStream stream) {
        SnowballProgram stemmer = getStemmer(lang);

        if (stemmer != null && times > 0) {
            for (int i = 0; i < times; i++) {
                stream = new SnowballFilter(stream, stemmer);
            }
        }

        return stream;
    }

    private static SnowballProgram getStemmer(String lang) {
        switch (lang.toUpperCase()) {
            case "EN":
                return new EnglishStemmer();
            case "PT":
                return new PortugueseStemmer();
            case "ES":
                return new SpanishStemmer();
            case "DE":
                return new GermanStemmer();
            case "FR":
                return new FrenchStemmer();
            case "SV":
                return new SwedishStemmer();
            case "IT":
                return new ItalianStemmer();
            case "NL":
                return new DutchStemmer();
            case "RU":
                return new RussianStemmer();

            case "AR":
            case "FA":
            case "ZH":
            case "KO":
                logger.warn("No stemmer is being used for '{}'", lang);
                return null;
        }
        return null;
    }
}
