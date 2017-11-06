package org.lambda3.indra.entity.threshold;

import com.google.auto.service.AutoService;
import org.lambda3.indra.factory.CachedIndraFactory;
import org.lambda3.indra.factory.IndraFactory;
import org.lambda3.indra.exception.IndraInvalidParameterException;

@AutoService(IndraFactory.class)
public class ThresholdFactory extends CachedIndraFactory<Threshold> {

    public ThresholdFactory() {
        super(Threshold.class);
    }

    @Override
    protected Threshold create(String name, String... params) throws IndraInvalidParameterException {
        switch (name) {
            case "auto":
                return new AutoThreshold();
            case "min":
                return new MinThreshold(Float.parseFloat(params[0]));
            case "max":
                return new MaxThreshold(Float.parseFloat(params[0]));
            case "top":
                return new TopThreshold(Integer.parseInt(params[0]));
            default:
                throw new IndraInvalidParameterException("invalid threshold descriptor.");
        }
    }

    @Override
    public Threshold getDefault() {
        return null;
    }

    @Override
    public String getName() {
        return IndraFactory.BUILT_IN_FACTORY;
    }
}
