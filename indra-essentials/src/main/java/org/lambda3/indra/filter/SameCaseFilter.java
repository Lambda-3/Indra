package org.lambda3.indra.filter;

public class SameCaseFilter implements Filter {

    @Override
    public boolean matches(String t1, String t2) {
        if ((Character.isUpperCase(t1.charAt(0)) && Character.isUpperCase(t2.charAt(0))) ||
                (Character.isLowerCase(t1.charAt(0)) && Character.isLowerCase(t2.charAt(0)))) {
            return false;
        }

        return true;
    }
}
