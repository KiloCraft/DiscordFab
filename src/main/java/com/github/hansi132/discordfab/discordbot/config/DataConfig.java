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
            properties.put("broadcastEnable", "*True or false*");
            properties.put("broadcastChannel", "*Paste what channel to broadcast to here*");
            properties.put("discordBroadcaster", "*Webhook url to cast Minecraft chat to*");
            properties.put("embedPicture", "Image link to use in the embeds.");
            properties.put("commandSpyBroadcaster", "*Webhook url to cast Minecraft commands to*");
            properties.put("SocialSpyBroadcaster", "Webhook url to cast msg/r commands to");
            properties.put("databaseUser", "*Specify the user of the database*");
            properties.put("databasePassword", "*Specify the password of the database*");
            properties.put("database", "*Specify the database to be used*");

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
