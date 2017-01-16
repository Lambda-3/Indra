package org.lambda3.indra.service.config;

/*-
 * ==========================License-Start=============================
 * Indra Web Service Module
 * --------------------------------------------------------------------
 * Copyright (C) 2016 - 2017 Lambda^3
 * --------------------------------------------------------------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
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
