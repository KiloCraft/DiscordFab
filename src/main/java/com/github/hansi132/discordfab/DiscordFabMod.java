package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.CommandSpyBroadcaster;
import com.github.hansi132.discordfab.discordbot.integration.DiscordBroadcaster;
import com.github.hansi132.discordfab.discordbot.util.LinkKeyCreator;
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

        ServerStartCallback.EVENT.register((server -> {
            KiloServer.getServer().registerEvent(new DiscordBroadcaster());
            KiloServer.getServer().registerEvent(new CommandSpyBroadcaster());
        }));

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("link").executes(context -> {

                Random random = new Random();
                int TestKey = 7404;
                int LinkKey = 1;
                String username = context.getSource().getName();
                ServerCommandSource src = context.getSource();

                try {
                    //Database
                    String db = new DataConfig().getProperty("database");
                    String dbUser = new DataConfig().getProperty("databaseUser");
                    String dbPassword = new DataConfig().getProperty("databasePassword");
                    Connection connection = DriverManager.getConnection(db, dbUser, dbPassword);

                    LinkKey = new LinkKeyCreator().checkKey(TestKey);
                    TextMessage text = new TextMessage("Your link key is: " + LinkKey);

                    String selectSql = "SELECT McUsername FROM linkedaccounts WHERE McUsername = ?;";
                    PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                    selectStatement.setString(1, username);
                    ResultSet resultSet = selectStatement.executeQuery();

                    if (resultSet.next()) {
                        TextMessage error = new TextMessage("&4You are already linked");
                        KiloChat.sendMessageTo(src, error);
                        return 0;
                    }

                    KiloChat.sendMessageTo(src, text);

                    String insertSql = "INSERT INTO linkedaccounts (LinkKey, McUsername) VALUES (?,?);";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setInt(1, LinkKey);
                    insertStatement.setString(2, username);
                    insertStatement.execute();

                    connection.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }


                return 1;
            }));
        });
    }
}
