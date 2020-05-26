package com.github.hansi132.DiscordFab;

import com.github.hansi132.DiscordFab.discordbot.config.DataConfig;
import net.fabricmc.api.DedicatedServerModInitializer;

public class DiscordFabMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        new DiscordFab(
                new DataConfig()
        );
    }
}
