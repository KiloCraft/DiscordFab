package com.github.hansi132.DiscordFab.discordbot.config;

import com.github.hansi132.DiscordFab.DiscordFab;
import com.github.hansi132.DiscordFab.discordbot.util.Variables;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

public class DataConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordFab.class);
    private final Properties properties = new Properties();
    private final File FILE = Variables.WORKING_PATH.resolve("data.properties").toFile();

    public DataConfig() {
        if (FILE.exists()) {
            try {
                this.loadProperties();
            } catch (IOException e) {
                LOGGER.error("Can not load the data properties!", e);
            }
        } else {
            properties.put("token", "*paste bot token here*");
            try {
                this.saveProperties();
            } catch (IOException e) {
                LOGGER.error("Can not generate the data properties!", e);
            }
        }
    }

    public void loadProperties() throws IOException {
        final InputStream in = new FileInputStream(FILE);
        properties.load(in);
    }

    public void saveProperties() throws IOException {
        final OutputStream out = new FileOutputStream(FILE);
        properties.store(out, "DiscordFab tokens properties");
    }

    public Properties getProperties() {
        return this.properties;
    }

    public String getProperty(@NotNull final String key) {
        return this.properties.getProperty(key);
    }

    public String getToken() {
        return this.properties.getProperty("token");
    }

}
