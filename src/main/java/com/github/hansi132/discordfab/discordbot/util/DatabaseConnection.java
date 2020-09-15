package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final DataConfig config = DiscordFab.getInstance().getDataConfig();

    public static Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(
                config.getProperty("database"),
                config.getProperty("databaseUser"),
                config.getProperty("databasePassword")
        );
    }

}
