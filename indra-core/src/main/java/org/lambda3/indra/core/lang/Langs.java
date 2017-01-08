package org.lambda3.indra.core.lang;

import org.lambda3.indra.common.client.Language;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//TODO: JavaDoc
public final class Langs {

    //TODO IndraAnalyzer is not thread safe. Workaround below until a final implementation that avoid create/discart object.
    private static Map<Tuple, IndraAnalyzer> analyzers = new ConcurrentHashMap<>();

    public static IndraAnalyzer getAnalyzer(Language lang, boolean stemming) {
        return analyzers.computeIfAbsent(new Tuple(lang, stemming),
                (t) -> new IndraAnalyzer(t.language, t.stemming));
    }

    public static IndraAnalyzer newInstanceAnalyzer(Language lang, boolean stemming) {
        return new IndraAnalyzer(lang, stemming);
    }

    private static final class Tuple {
        final Language language;
        final boolean stemming;

        public Tuple(Language language, boolean stemming) {
            this.language = language;
            this.stemming = stemming;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Tuple that = (Tuple) o;

            if (stemming != that.stemming) return false;
            return language == that.language;

        }

        @Override
        public int hashCode() {
            int result = language.hashCode();
            result = 31 * result + (stemming ? 1 : 0);
            return result;
        }
    }

}
