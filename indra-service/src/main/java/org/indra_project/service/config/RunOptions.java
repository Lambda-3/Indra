package org.indra_project.service.config;

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
