package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final Connection connection;

    public DatabaseConnection() throws ClassNotFoundException, SQLException {
        DataConfig config = DiscordFab.getInstance().getDataConfig();
        //Database
        Class.forName("com.mysql.jdbc.Driver");
        String dbPassword = config.getProperty("databasePassword");
        String dbUser = config.getProperty("databaseUser");
        String db = config.getProperty("database");
        connection = DriverManager.getConnection(db, dbUser, dbPassword);
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
