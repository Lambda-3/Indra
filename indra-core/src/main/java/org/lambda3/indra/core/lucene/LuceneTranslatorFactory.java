package org.lambda3.indra.core.lucene;

import org.lambda3.indra.request.AbstractBasicRequest;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.lambda3.indra.core.translation.TranslatorFactory;
import org.lambda3.indra.exception.TranslatorNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

public class LuceneTranslatorFactory extends TranslatorFactory {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private File baseDir;

    public LuceneTranslatorFactory(File baseDir) {
        this.baseDir = baseDir;
        if (!baseDir.canRead()) {
            throw new RuntimeException("Can't read data from " + baseDir);
        }
        logger.debug("Setting baseDir to {}", this.baseDir);

    }

    @Override
    public Collection<String> getAvailableModels() {
        Collection<String> results = new LinkedList<>();

        try {
            if (baseDir.exists() && baseDir.isDirectory()) {
                File[] data = baseDir.listFiles(File::isDirectory);
                for (File d : data) {
                    results.add(d.getName());
                }
            }
        } catch (NullPointerException e) {
            logger.error("LuceneTranslatorFactory baseDir is wrongly configured or there is an I/O error\n{}", e.getMessage());
        }

        return results;
    }

    @Override
    protected IndraTranslator doCreate(AbstractBasicRequest<?> request) {
        String path = createKey(request);
        File dir = new File(path);
        if (dir.exists()) {
            return new LuceneTranslator(path);
        } else {
            throw new TranslatorNotFoundException(request.getLanguage());
        }
    }

    @Override
    protected String createKey(AbstractBasicRequest<?> request) {
        return Paths.get(this.baseDir.getAbsolutePath(), request.getLanguage().toLowerCase()).toString();
    }

    @Override
    public void close() throws IOException {
        //do nothing
    }
}
