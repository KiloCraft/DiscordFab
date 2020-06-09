package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.util.Variables;
import net.fabricmc.api.DedicatedServerModInitializer;

import java.io.File;

public class DiscordFabMod implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        final File CONFIG_FILE = Variables.CONFIG_PATH.toFile();
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.mkdirs();
        }

        new DiscordFab(
                new DataConfig()
        );

    }
}
