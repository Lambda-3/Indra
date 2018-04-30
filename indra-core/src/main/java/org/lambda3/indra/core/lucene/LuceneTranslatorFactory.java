package org.lambda3.indra.core.lucene;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * ==========================License-End===============================
 */

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
