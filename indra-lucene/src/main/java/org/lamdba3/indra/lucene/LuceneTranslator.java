package org.lamdba3.indra.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.lambda3.indra.MutableTranslatedTerm;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Paths;
import java.util.*;

public class LuceneTranslator extends IndraTranslator {

    private static final String LEX_INDEX = "lex";

    private static final String TERM_FIELD = "term";
    private static final String TRANSLATION_FIELD = "tr";
    private static final String NULL_VALUE = "NULL";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private IndexReader reader;
    private IndexSearcher searcher;


    LuceneTranslator(String dataDir) {

        try {
            this.reader = DirectoryReader.open(FSDirectory.open(Paths.get(dataDir, LEX_INDEX)));
            this.searcher = new IndexSearcher(reader);

        } catch (IOException e) {
            logger.error("Lucene basedir is wrongly configured or there is an I/O error\n{}", e.getMessage());
            //TODO throw exception
            e.printStackTrace();
        }
    }

    @Override
    public void translate(List<MutableTranslatedTerm> terms) {

        Set<String> allTokens = new HashSet<>();
        terms.forEach(t -> allTokens.addAll(t.getAnalyzedTokens()));
        logger.debug("Translating {} MutableAnalyzedTerms, resulting in {} tokens", terms.size(), allTokens.size());

        Map<String, List<String>> translations = doTranslate(allTokens);
        for (MutableTranslatedTerm term : terms) {

            for (String token : term.getAnalyzedTokens()) {
                List<String> tokenTranslations = translations.get(token);
                if (tokenTranslations != null) {
                    term.putTranslatedTokens(token, tokenTranslations);
                }
            }
        }
    }

    private Map<String, List<String>> doTranslate(Set<String> terms) {

        Map<String, List<String>> res = new HashMap<>();

        try {
            TopDocs topDocs = LuceneUtils.getTopDocs(searcher, terms, TERM_FIELD);


            if (topDocs != null) {
                for (ScoreDoc sd : topDocs.scoreDocs) {
                    Document doc = searcher.doc(sd.doc);
                    Map<String, Double> content = convert(doc.getBinaryValue(TRANSLATION_FIELD).bytes);
                    res.put(doc.get(TERM_FIELD), getRelevantTranslations(content));
                }
            }

        } catch (IOException e) {
            logger.error(e.getMessage());
            //TODO throw new expection here.
            e.printStackTrace();
        }

        return res;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Double> convert(byte[] content) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(content));
            return (Map<String, Double>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error(e.getMessage());
            //TODO throw new expection here.
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() throws IOException {
        this.reader.close();
    }
}
