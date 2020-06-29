package com.github.hansi132.discordfab;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final Connection connection;

    public DatabaseConnection() throws ClassNotFoundException, SQLException {
        //Database
        Class.forName("com.mysql.jdbc.Driver");
        String dbPassword = new DataConfig().getProperty("databasePassword");
        String dbUser = new DataConfig().getProperty("databaseUser");
        String db = new DataConfig().getProperty("database");
        connection = DriverManager.getConnection(db, dbUser, dbPassword);
    }

    public Connection getConnection() {
        return connection;
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }
}
