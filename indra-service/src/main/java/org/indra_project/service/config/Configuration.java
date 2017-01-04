package org.indra_project.service.config;


import com.mongodb.MongoClientURI;
import org.indra_project.common.client.Language;
import org.indra_project.common.client.Model;

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
