package org.lambda3.indra;

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
