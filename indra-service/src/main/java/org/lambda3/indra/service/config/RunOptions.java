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

import net.sourceforge.argparse4j.annotation.Arg;

import java.io.File;

public class RunOptions {

    @Arg(dest = "mock")
    public boolean mock;

    @Arg(dest = "httpPort")
    public int httpPort;

    @Arg(dest = "configFile")
    public File configFile;

    @Override
    public String toString() {
        return String.format("httpPort: %d,\n configFile: %s", httpPort, configFile);
    }
}
