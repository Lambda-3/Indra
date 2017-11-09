package org.lambda3.indra;

/*-
 * ==========================License-Start=============================
 * Indra Client Module
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

public class AnalyzedPair {
    protected final TextPair pair;
    protected AnalyzedTerm t1;
    protected AnalyzedTerm t2;

    public AnalyzedPair(TextPair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("pair can't be null");
        }
        this.pair = pair;
    }

    public void setAnalyzedTerm1(AnalyzedTerm t1) {
        this.t1 = t1;
    }

    public void setAnalyzedTerm2(AnalyzedTerm t2) {
        this.t2 = t2;
    }

    public TextPair getTextPair() {
        return this.pair;
    }

    public AnalyzedTerm getAnalyzedT1() {
        return t1;
    }

    public AnalyzedTerm getAnalyzedT2() {
        return t2;
    }

}
