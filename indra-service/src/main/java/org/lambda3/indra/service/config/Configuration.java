package org.lambda3.indra.service.config;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
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


import com.mongodb.MongoClientURI;
import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.Model;

import java.util.ArrayList;
import java.util.Collection;

public class Configuration {

    public String httpBasePath = "/indra/v1";

    public String mongoURI = "mongodb://localhost:27017";

    public Collection<Model> models = new ArrayList<Model>() {{
        for (Model model : Model.values()) {
            add(model);
        }
    }};

    public Collection<Language> languages = new ArrayList<Language>() {{
        for (Language lang : Language.values()) {
            add(lang);
        }
    }};

    public Security security;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nhttpBasePath: '%s'\n");
        sb.append("models: %s\n");
        sb.append("languages: %s\n");
        sb.append(String.format("mongoDBHosts: %s", new MongoClientURI(mongoURI).getHosts()));
        return String.format(sb.toString(), httpBasePath, models, languages);
    }
}
