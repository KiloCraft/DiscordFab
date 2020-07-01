package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final DataConfig CONFIG = DiscordFab.getInstance().getDataConfig();
    private Connection connection;

    public DatabaseConnection() throws ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
    }

    public Connection connect() throws SQLException {
        this.connection = DriverManager.getConnection(
                CONFIG.getProperty("databasePassword"),
                CONFIG.getProperty("databaseUser"),
                CONFIG.getProperty("database")
        );

        return this.connection;
    }

    public Connection getConnection() {
        if (this.connection == null) {
            throw new IllegalStateException("A connection hasn't been made!");
        }

        return this.connection;
    }

    public void close() throws SQLException {
        connection.close();
    }
}
