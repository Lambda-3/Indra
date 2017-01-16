package org.lambda3.indra.rest.resources;

/*-
 * ==========================License-Start=============================
 * Indra REST Module
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
import org.lambda3.indra.common.client.TextPair;

import java.util.List;

public final class RelatednessRequest {

    public String corpus;
    public Model model;
    public Language language;
    public List<TextPair> pairs;
    public ScoreFunction scoreFunction;

    @Override
    public String toString() {
        return "RelatednessRequest{" +
                "corpus='" + corpus + '\'' +
                ", model=" + model +
                ", language=" + language +
                ", pairs=" + pairs +
                ", scoreFunction=" + scoreFunction +
                '}';
    }
}
