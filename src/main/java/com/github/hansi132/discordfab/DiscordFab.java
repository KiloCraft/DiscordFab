package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.BotMain;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class DiscordFab implements DedicatedServerModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordFab.class);

    @Override
    public void onInitializeServer() {
        try {
            new BotMain();
        } catch (LoginException e) {
            LOGGER.warn(String.valueOf(e));
        }
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
