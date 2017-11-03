package org.lambda3.indra.core.filter;

import com.google.auto.service.AutoService;
import org.lambda3.indra.factory.CachedIndraFactory;
import org.lambda3.indra.factory.IndraFactory;
import org.lambda3.indra.exception.IndraInvalidParameterException;

@AutoService(IndraFactory.class)
public class FilterFactory extends CachedIndraFactory<Filter> {

    private static final String GOOGLE_NEWS_CLEANER = "gnc";

    public FilterFactory() {
        super(Filter.class);
    }

    @Override
    protected Filter create(String name, String... params) throws IndraInvalidParameterException {
        switch (name) {
            case GOOGLE_NEWS_CLEANER:
                return FilterChain.get().add(new SameCaseFilter()).add(new DistanceStringFilter(5, 2)).
                        add(new T2StringFilter("#", "@", "$", ".", "®", "§", "'", "`", "£", "€", "™", "{", "}", "[", "]"));
            default:
                throw new IndraInvalidParameterException(null);
        }
    }

    @Override
    public Filter getDefault() {
        return null;
    }

    @Override
    public String getName() {
        return IndraFactory.BUILT_IN_FACTORY;
    }
}
