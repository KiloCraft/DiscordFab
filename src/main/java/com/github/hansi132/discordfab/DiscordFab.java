package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.CommandManager;
import com.github.hansi132.discordfab.discordbot.DiscordFabBot;
import com.github.hansi132.discordfab.discordbot.ChatSynchronizer;
import com.github.hansi132.discordfab.discordbot.Listener;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.config.DiscordFabConfig;
import com.github.hansi132.discordfab.discordbot.config.MainConfig;
import com.github.hansi132.discordfab.discordbot.util.EmbedUtil;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;

public class DiscordFab {
    public static final Logger LOGGER = LogManager.getLogger("DiscordFab");
    private static DiscordFab INSTANCE;
    private static ShardManager BOT;
    private boolean isDevelopment = false;
    private final DataConfig dataConfig;
    private final DiscordFabConfig config;
    private final CommandManager commandManager;
    private final ChatSynchronizer chatSynchronizer;
    private final Guild guild;
    private EmbedUtil embedUtil;

    DiscordFab(@NotNull final DataConfig dataConfig) {
        INSTANCE = this;
        this.dataConfig = dataConfig;
        this.config = new DiscordFabConfig();
        this.config.load();
        this.isDevelopment = this.dataConfig.getProperties().containsKey("debug");

        this.commandManager = new CommandManager();
        this.chatSynchronizer = new ChatSynchronizer();
        this.embedUtil = new EmbedUtil();

        try {
            BOT = new DiscordFabBot(
                    dataConfig.getToken(),
                    new Listener()
            ).getShardManager();
            BOT.setActivity(Activity.playing("50kilo.org"));

            if (isDevelopment) {
                LOGGER.info("**** DiscordFab IS RUNNING IN DEBUG/DEVELOPMENT MODE!");
                BOT.setActivity(Activity.playing("Debugging"));
            }

            LOGGER.info("Successfully logged in");
        } catch (LoginException e) {
            LOGGER.fatal("Can not log into the bot!", e);
        }

        this.guild = BOT.getGuildById(Long.parseLong(dataConfig.getProperty("guild")));
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

    public MainConfig getConfig() {
        return this.config.get();
    }

    public ChatSynchronizer getChatSynchronizer() {
        return this.chatSynchronizer;
    }

    public static ShardManager getBot() {
        return DiscordFab.BOT;
    }

    public Guild getGuild() {
        return this.guild;
    }

    public EmbedUtil getEmbedUtil() {
        return this.embedUtil;
    }
}
