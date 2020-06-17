package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.CommandSpyBroadcaster;
import com.github.hansi132.discordfab.discordbot.integration.DiscordBroadcaster;
import com.github.hansi132.discordfab.discordbot.util.Variables;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;

import java.io.File;
import java.util.Random;

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

        ServerStartCallback.EVENT.register((server -> {
            KiloServer.getServer().registerEvent(new DiscordBroadcaster());
            KiloServer.getServer().registerEvent(new CommandSpyBroadcaster());
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("link").executes(context -> {
                Random random = new Random();
                TextMessage text = new TextMessage("Your link key is: " + random.nextInt(10000));
                //TODO check the db if the string of numbers is already present
                String username = context.getSource().getName();
                //TODO send the username to the db to store it together with the ingamename
                ServerCommandSource src = context.getSource();
                KiloChat.sendMessageTo(src, text);
                return 1;
            }));
        });
    }
}
