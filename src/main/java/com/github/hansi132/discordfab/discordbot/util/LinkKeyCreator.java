package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;

import java.sql.*;
import java.util.Random;

public class LinkKeyCreator {
    public LinkKeyCreator() {

    }

    public int checkKey(int LinkKey) throws SQLException {
        Random random = new Random();

        //Database
        String db = new DataConfig().getProperty("database");
        String dbUser = new DataConfig().getProperty("databaseUser");
        String dbPassword = new DataConfig().getProperty("databasePassword");
        Connection connection = DriverManager.getConnection(db, dbUser, dbPassword);

        String selectSql = "SELECT LinkKey FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, LinkKey);
        ResultSet checkDuplicateRs = selectStatement.executeQuery();

        if (checkDuplicateRs.next()) {
            LinkKey = random.nextInt(10000);
            this.checkKey(LinkKey);
        }

        return LinkKey;
    }
}
