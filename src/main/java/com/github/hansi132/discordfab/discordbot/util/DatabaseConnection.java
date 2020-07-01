package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final DataConfig config = DiscordFab.getInstance().getDataConfig();
    private final java.sql.Connection connection;

    public DatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.connection = DriverManager.getConnection(
                config.getProperty("database"),
                config.getProperty("databaseUser"),
                config.getProperty("databasePassword")
        );
    }

    public java.sql.Connection get() {
        return this.connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
