package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.CommandManager;
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
    private final CommandManager commandManager;

    DiscordFab(@NotNull final DataConfig dataConfig) {
        INSTANCE = this;
        this.dataConfig = dataConfig;
        this.isDevelopment = this.dataConfig.getPROPERTIES().containsKey("debug");

        this.commandManager = new CommandManager(this);

        try {
            bot = new DiscordFabBot(
                    dataConfig.getToken(),
                    new Listener()
            ).getShardManager();
            bot.setActivity(Activity.playing("50kilo.org"));

            if (isDevelopment) {
                LOGGER.info("**** DiscordFab IS RUNNING IN DEBUG/DEVELOPMENT MODE!");
                bot.setActivity(Activity.playing("Debugging"));
            }

        } catch (LoginException e) {
            LOGGER.fatal("Can not log into the bot!", e);
        } finally {
            LOGGER.info("Successfully logged in");
        }

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

    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    public DataConfig getDataConfig() {
        return this.dataConfig;
    }

    public static ShardManager getBot() {
        return DiscordFab.bot;
    }
}
