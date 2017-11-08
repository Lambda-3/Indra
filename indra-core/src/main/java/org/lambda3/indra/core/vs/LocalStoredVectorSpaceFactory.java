package org.lambda3.indra.core.vs;

import org.lambda3.indra.request.AbstractBasicRequest;
import org.lambda3.indra.core.translation.IndraTranslator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;

public abstract class LocalStoredVectorSpaceFactory extends VectorSpaceFactory {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private File baseDir;

    public LocalStoredVectorSpaceFactory(File baseDir) {
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

                File[] models = baseDir.listFiles(File::isDirectory);
                for (File model : models) {
                    File[] langs = model.listFiles(File::isDirectory);
                    for (File lang : langs) {
                        File[] corpora = lang.listFiles(File::isDirectory);
                        for (File corpus : corpora) {
                            results.add(String.format("%s-%s-%s", model.getName(), lang.getName(), corpus.getName()));
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            logger.error("Annoy basedir is wrongly configured or there is an I/O error\n{}", e.getMessage());
        }

        return results;
    }

    @Override
    protected String createKey(AbstractBasicRequest request) {
        return createVSFile(request).getAbsolutePath() + request.isMt();
    }

    protected File createVSFile(AbstractBasicRequest request) {
        String lang = request.isMt() ? IndraTranslator.DEFAULT_TRANSLATION_TARGET_LANGUAGE.toLowerCase() :
                request.getLanguage().toLowerCase();
        return Paths.get(baseDir.getAbsolutePath(), request.getModel().toLowerCase(), lang,
                request.getCorpus().toLowerCase()).toFile();
    }

}
