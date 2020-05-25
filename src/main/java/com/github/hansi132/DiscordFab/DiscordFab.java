package com.github.hansi132.DiscordFab;

import com.github.hansi132.DiscordFab.DiscordBot.botMain;
import net.fabricmc.api.DedicatedServerModInitializer;

import javax.security.auth.login.LoginException;

public class DiscordFab implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        try {
            new botMain();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
}
