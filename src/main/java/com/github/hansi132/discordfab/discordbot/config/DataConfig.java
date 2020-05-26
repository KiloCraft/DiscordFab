package com.github.hansi132.discordfab.discordbot.config;

import com.github.hansi132.discordfab.discordbot.util.Variables;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

public class DataConfig {
    private static final Logger LOGGER = LogManager.getLogger();
    private final Properties properties = new Properties();
    private final File FILE = Variables.CONFIG_PATH.resolve("tokens.properties").toFile();

    public DataConfig() {
        if (FILE.exists()) {
            try {
                this.loadProperties();
            } catch (IOException e) {
                LOGGER.error("Can not load the data properties!", e);
            }
        } else {
            properties.put("token", "*paste bot token here*");
            properties.put("webhook_url", "*paste webhook_url here*");
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
