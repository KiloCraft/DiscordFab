package com.github.hansi132.DiscordFab;

import com.github.hansi132.DiscordFab.DiscordBot.BotMain;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.IOException;

public class DiscordFab implements DedicatedServerModInitializer {
    final Logger logger = LoggerFactory.getLogger(DiscordFab.class);

    @Override
    public void onInitializeServer() {
        try {
            new BotMain();
        } catch (LoginException e) {
            logger.warn(String.valueOf(e));
        }


    }
}
