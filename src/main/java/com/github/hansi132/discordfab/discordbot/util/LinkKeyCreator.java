package com.github.hansi132.discordfab.discordbot.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LinkKeyCreator {
    public LinkKeyCreator() {

    }

    public int checkKey(int linkKey) throws SQLException, ClassNotFoundException {
        Random random = new Random();

        Connection connection = new DatabaseConnection().get();

        String selectSql = "SELECT LinkKey FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStatement = connection.prepareStatement(selectSql);
        selectStatement.setInt(1, linkKey);
        ResultSet checkDuplicateRs = selectStatement.executeQuery();

        if (checkDuplicateRs.next()) {
            linkKey = random.nextInt(10000);
            connection.close();
            return this.checkKey(linkKey);
        }
        connection.close();
        return linkKey;
    }
}
