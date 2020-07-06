package com.github.hansi132.discordfab.discordbot.config;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.Variables;
import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.DefaultObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.File;
import java.io.IOException;

public class DiscordFabConfig {
    private MainConfig config;
    private ConfigurationNode node;

    public DiscordFabConfig() {
    }

    public void load() {
        try {
            File file = Variables.CONFIG_PATH.resolve("discord_fab.conf").toFile();
            ConfigurationLoader<CommentedConfigurationNode> loader = HoconConfigurationLoader.builder()
                    .setFile(file).build();

            if (!file.exists()) {
                file.createNewFile();
            }

            node = loader.load(
                    ConfigurationOptions.defaults()
                            .setHeader(MainConfig.HEADER)
                            .setObjectMapperFactory(DefaultObjectMapperFactory.getInstance())
                            .setShouldCopyDefaults(true)
            );
            config = node.getValue(TypeToken.of(MainConfig.class), new MainConfig());

            loader.save(node);
        } catch (IOException e) {
            DiscordFab.LOGGER.error("Could not generate the Config File!", e);
        } catch (ObjectMappingException e) {
            DiscordFab.LOGGER.error("Exception while generating the Config File!", e);
        }
    }

    public MainConfig get() {
        return this.config;
    }
}
