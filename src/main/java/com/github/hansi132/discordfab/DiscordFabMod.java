package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.commands.EssentialsDiscordLinkCommand;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.*;
import com.github.hansi132.discordfab.discordbot.util.Variables;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.KiloServer;

import java.io.File;
import java.util.HashMap;

public class DiscordFabMod implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        final File CONFIG_FILE = Variables.CONFIG_PATH.toFile();
        if (!CONFIG_FILE.exists()) {
            CONFIG_FILE.mkdirs();
        }

        DataConfig config = new DataConfig(
                new HashMap<String, Object>() {{
                    this.put("token", "*paste bot token here*");
                    this.put("webhook_url", "*paste webhook_url here*");
                    this.put("broadcastEnable", "*True or false*");
                    this.put("broadcastChannel", "*Paste what channel to broadcast to here*");
                    this.put("discordBroadcaster", "*Webhook url to cast Minecraft chat to*");
                    this.put("embedPicture", "Image link to use in the embeds.");
                    this.put("commandSpyBroadcaster", "*Webhook url to cast Minecraft commands to*");
                    this.put("SocialSpyBroadcaster", "Webhook url to cast msg/r commands to");
                    this.put("databaseUser", "*Specify the user of the database*");
                    this.put("databasePassword", "*Specify the password of the database*");
                    this.put("database", "*Specify the database to be used*");
                    this.put("guild", "*Specify the guild id");
                    this.put("role", "*Specify what the linked role should be");
                }}
        );

        new DiscordFab(config);

        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            KiloServer.getServer().registerEvent(new DiscordBroadcaster());
            KiloServer.getServer().registerEvent(new CommandSpyBroadcaster());
            KiloServer.getServer().registerEvent(new SocialSpyBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerJoinBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerLeaveBroadcaster());

            KiloEssentials.getInstance().getCommandHandler().register(
                    new EssentialsDiscordLinkCommand("link", new String[]{"discord_link"})
            );
        }));
    }
}
