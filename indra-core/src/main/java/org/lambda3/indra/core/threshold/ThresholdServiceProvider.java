package org.lambda3.indra.core.threshold;

import org.lambda3.indra.core.IndraServiceProvider;

public class ThresholdServiceProvider extends IndraServiceProvider<Threshold> {

    public ThresholdServiceProvider() {
        super(Threshold.class);
    }

    @Override
    public Threshold get(String type) {
        Threshold t = super.get(type);
        if (t == null) {
            String[] parts = type.split("-");
            if (parts.length > 1) {
                t = super.get(parts[0]);
                t.configure(parts[1]);
                objects.put(type, t);
                return t;
            }
        }

        return null;
    }
}
