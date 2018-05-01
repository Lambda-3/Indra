package org.lambda3.indra;

/*-
 * ==========================License-Start=============================
 * Indra Essentials Module
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

import org.lambda3.indra.corpus.CorpusMetadata;
import org.lambda3.indra.corpus.CorpusMetadataBuilder;
import org.lambda3.indra.model.ModelMetadata;
import org.lambda3.indra.util.JSONUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

public class MetadataIO {

    public static final String CORPUS_METADATA_FILE_NAME = "corpus.metadata";
    public static final String MODEL_METADATA_FILE_NAME = "model.metadata";

    private static String getFileName(Class<? extends Metadata> clazz) {
        return clazz == CorpusMetadata.class ? CORPUS_METADATA_FILE_NAME :
                MODEL_METADATA_FILE_NAME;
    }

    public static void write(String modelDir, Metadata metadata) {
        try {
            JSONUtil.writeMapAsJson(metadata.asMap(), Paths.get(modelDir, getFileName(metadata.getClass())).toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static <M extends Metadata> M load(String dir, Class<M> clazz) {


        File metadataFile = Paths.get(dir, getFileName(clazz)).toFile();
        Map<String, Object> map = JSONUtil.loadJSONAsMap(metadataFile);

        if (clazz == CorpusMetadata.class) {
            return (M) CorpusMetadataBuilder.fromMap(map);
        } else {
            return (M) new ModelMetadata(map);
        }
    }
}
