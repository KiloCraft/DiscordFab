package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.ChatSynchronizer;
import com.github.hansi132.discordfab.discordbot.CommandManager;
import com.github.hansi132.discordfab.discordbot.DiscordFabBot;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.config.DiscordFabConfig;
import com.github.hansi132.discordfab.discordbot.config.MainConfig;
import com.github.hansi132.discordfab.discordbot.listener.Listener;
import com.github.hansi132.discordfab.discordbot.util.EmbedUtil;
import com.github.hansi132.discordfab.discordbot.util.InviteTracker;
import com.github.hansi132.discordfab.discordbot.util.OnlinePlayerUpdater;
import com.github.hansi132.discordfab.discordbot.util.user.LinkedUserCache;
import net.dv8tion.jda.api.OnlineStatus;
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
    private static ShardManager SHARD_MANAGER;
    private boolean isDevelopment;
    private final DataConfig dataConfig;
    private final DiscordFabConfig config;
    private final CommandManager commandManager;
    private final ChatSynchronizer chatSynchronizer;
    private final EmbedUtil embedUtil;
    private final InviteTracker inviteTracker;
    private OnlinePlayerUpdater onlinePlayerUpdater;
    private LinkedUserCache userCache;

    DiscordFab(@NotNull final DataConfig dataConfig) {
        INSTANCE = this;
        this.dataConfig = dataConfig;
        this.config = new DiscordFabConfig();
        this.config.load();
        this.isDevelopment = this.dataConfig.getProperties().containsKey("debug");

        this.commandManager = new CommandManager();
        this.chatSynchronizer = new ChatSynchronizer();
        this.inviteTracker = new InviteTracker();
        this.embedUtil = new EmbedUtil();

        try {
            SHARD_MANAGER = new DiscordFabBot(
                    dataConfig.getToken(),
                    new Listener()
            ).getShardManager();

            if (isDevelopment) {
                LOGGER.info("**** DiscordFab IS RUNNING IN DEBUG/DEVELOPMENT MODE!");
            }

            LOGGER.info("Successfully logged in");
        } catch (LoginException e) {
            LOGGER.fatal("Can not log into the bot!", e);
        }
        this.userCache = new LinkedUserCache();
        onLoad();
    }

    public void onLoad() {
        LOGGER.info("Loading DiscordFab..");
        this.dataConfig.load();
        this.isDevelopment = this.dataConfig.getProperties().containsKey("debug");
        this.config.load();
        final Activity.ActivityType activityType = config.get().activity.getActivityType();
        final String activityValue = config.get().activity.value;
        final OnlineStatus status = config.get().activity.getOnlineStatus();
        this.setActivity(activityType, activityValue);
        this.chatSynchronizer.load();
        SHARD_MANAGER.setStatus(status);
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

    public InviteTracker getInviteTracker() {
        return this.inviteTracker;
    }


    public static ShardManager getBot() {
        return DiscordFab.SHARD_MANAGER;
    }

    public Guild getGuild() {
        return SHARD_MANAGER.getGuildById(Long.parseLong(this.dataConfig.getProperty("guild")));
    }

    public EmbedUtil getEmbedUtil() {
        return this.embedUtil;
    }

    void shutdown() {
        this.chatSynchronizer.shutdown();
    }

    public void setActivity(@NotNull final Activity.ActivityType type, @NotNull final String value) {
        if (value.equalsIgnoreCase("online")) {
            if (onlinePlayerUpdater != null) {
                if (onlinePlayerUpdater.getState() != Thread.State.NEW) {
                    onlinePlayerUpdater.interrupt();
                    onlinePlayerUpdater = new OnlinePlayerUpdater(type);
                }
            } else {
                onlinePlayerUpdater = new OnlinePlayerUpdater(type);
            }

            onlinePlayerUpdater.start();
            return;
        } else {
            onlinePlayerUpdater.interrupt();
        }

        SHARD_MANAGER.setActivity(Activity.of(type, value));
    }

    public LinkedUserCache getUserCache() {
        return userCache;
    }
}
