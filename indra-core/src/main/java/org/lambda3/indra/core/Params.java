package org.lambda3.indra.core;

/*-
 * ==========================License-Start=============================
 * Indra Core Module
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

import org.lambda3.indra.common.client.Language;
import org.lambda3.indra.common.client.Model;
import org.lambda3.indra.common.client.ScoreFunction;

public final class Params {
    public final String corpusName;
    public final Language language;
    public final Model model;
    public final ScoreFunction func;

    public Params(String corpusName, ScoreFunction func, Language language, Model model) {
        if (corpusName == null || func == null || language == null || model == null) {
            throw new IllegalArgumentException("All arguments are mandatory!");
        }

        this.corpusName = corpusName;
        this.func = func;
        this.language = language;
        this.model = model;
    }

    public boolean useStemming() {
        return true; //TODO: Will change or varies with the other params?
    }

    public String getDBName() {
        return String.format("%s-%s-%s",
                model.name().toLowerCase(),
                language.name().toLowerCase(),
                corpusName.toLowerCase());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Params params = (Params) o;

        if (!corpusName.equals(params.corpusName)) return false;
        if (language != params.language) return false;
        if (model != params.model) return false;
        return func == params.func;

    }

    @Override
    public int hashCode() {
        int result = corpusName.hashCode();
        result = 31 * result + language.hashCode();
        result = 31 * result + model.hashCode();
        result = 31 * result + func.hashCode();
        return result;
    }

}
