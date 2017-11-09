package org.lambda3.indra.filter;

import java.util.Arrays;
import java.util.List;

public class T2StringFilter implements Filter {
    private List<String> ss;

    public T2StringFilter(String... ss) {
        this.ss = Arrays.asList(ss);
    }

    @Override
    public boolean matches(String t1, String t2) {
        for (String s : ss) {
            if (t2.contains(s)) {
                return true;
            }
        }
        return false;
    }
}
