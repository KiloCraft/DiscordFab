package com.github.hansi132.discordfab.discordbot.config;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.Variables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataConfig {
    private static final Properties properties = new Properties();
    private static final File FILE = Variables.WORKING_PATH.resolve("data.properties").toFile();

    public DataConfig() throws IOException {
        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException e) {
                DiscordFab.getLogger().error("Can not generate the data properties file!", e);
            }

            //TODO: Add the default values
        }

        final InputStream in = new FileInputStream(FILE);
        properties.load(in);
    }
}
