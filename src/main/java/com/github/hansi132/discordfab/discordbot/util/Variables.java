package com.github.hansi132.discordfab.discordbot.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Variables {
    public static final String WORKING_DIR = System.getProperty("user.dir");
    public static final Path WORKING_PATH = Paths.get(WORKING_DIR);
}
