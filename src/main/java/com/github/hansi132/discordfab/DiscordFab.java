package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.DiscordFabBot;
import com.github.hansi132.discordfab.discordbot.Listener;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class DiscordFab {
    private static final Logger LOGGER = LogManager.getLogger();
    private static DiscordFab INSTANCE;
    private static ShardManager bot;
    private boolean isDevelopment = false;
    private final DataConfig dataConfig;

    DiscordFab(@NotNull final DataConfig dataConfig) {
        this.dataConfig = dataConfig;
        this.isDevelopment = this.dataConfig.getProperties().containsKey("debug");

        try {
            bot = new DiscordFabBot(
                    dataConfig.getToken(),
                    new Listener()
            ).getShardManager();

            if (isDevelopment) {
                LOGGER.info("**** DiscordFab IS RUNNING IN DEBUG/DEVELOPMENT MODE!");
                bot.setActivity(Activity.playing("Debugging"));
            }
        } catch (LoginException e) {
            LOGGER.fatal("Can not log into the bot!", e);
        } finally {
            LOGGER.info("Successfully logged in");
        }


        INSTANCE = this;
    }

    public boolean isDevelopment() {
        return this.isDevelopment;
    }

    public static DiscordFab getInstance() {
        if (INSTANCE == null) {
            throw new RuntimeException("Its too early to get a static instance of DiscordFab!");
        }

        return INSTANCE;
    }
}
