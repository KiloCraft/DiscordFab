package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.*;
import com.github.hansi132.discordfab.discordbot.util.LinkKeyCreator;
import com.github.hansi132.discordfab.discordbot.util.Variables;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;

import java.io.File;
import java.sql.*;
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

        ServerLifecycleEvents.SERVER_STARTED.register((server -> {
            KiloServer.getServer().registerEvent(new DiscordBroadcaster());
            KiloServer.getServer().registerEvent(new CommandSpyBroadcaster());
            KiloServer.getServer().registerEvent(new SocialSpyBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerJoinBroadcaster());
            KiloServer.getServer().registerEvent(new PlayerLeaveBroadcaster());
        }));


        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("link").executes(context -> {

                Random random = new Random();
                int TestKey = random.nextInt(10000);
                int LinkKey;
                String username = context.getSource().getName();
                ServerCommandSource src = context.getSource();
                String uuid = src.getPlayer().getUuid().toString();

                try {
                    //Database
                    Connection connection = new DatabaseConnection().getConnection();

                    LinkKey = new LinkKeyCreator().checkKey(TestKey);
                    TextMessage text = new TextMessage("Your link key is: " + LinkKey);

                    String selectSql = "SELECT McUUID FROM linkedaccounts WHERE McUUID = ?;";
                    PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                    selectStatement.setString(1, uuid);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        TextMessage error = new TextMessage("&cYou are already linked");
                        KiloChat.sendMessageTo(src, error);
                        return 0;
                    }

                    KiloChat.sendMessageTo(src, text);
                    LuckPerms api = LuckPermsProvider.get();

                    System.out.println(api.getUserManager().getUser(username).getPrimaryGroup());

                    String insertSql = "INSERT INTO linkedaccounts (LinkKey, McUUID, McUsername) VALUES (?,?,?);";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setInt(1, LinkKey);
                    insertStatement.setString(2, uuid);
                    insertStatement.setString(3, username);
                    insertStatement.execute();

                    connection.close();

                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }


                return 1;
            }));
        });
    }
}
