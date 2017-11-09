package org.lambda3.indra.core.lucene;

import org.lambda3.indra.request.AbstractBasicRequest;
import org.lambda3.indra.core.vs.LocalStoredVectorSpaceFactory;
import org.lambda3.indra.core.vs.VectorSpace;
import org.lambda3.indra.exception.ModelNotFoundException;

import java.io.File;

public class LuceneVectorSpaceFactory extends LocalStoredVectorSpaceFactory {

    public LuceneVectorSpaceFactory(File baseDir) {
        super(baseDir);
    }

    @Override
    protected VectorSpace doCreate(AbstractBasicRequest request) {
        File vsDirFile = createVSFile(request);

        if (vsDirFile.exists() && vsDirFile.isDirectory()) {
            return new LuceneVectorSpace(vsDirFile.getAbsolutePath());
        } else {
            throw new ModelNotFoundException(String.format("%s-%s-%s", request.getModel(),
                    request.getLanguage(), request.getCorpus()));
        }
    }
}
