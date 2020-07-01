package com.github.hansi132.discordfab.discordbot.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class LinkKeyCreator {
    public LinkKeyCreator() {

    }

    public int checkKey(int LinkKey) throws SQLException, ClassNotFoundException {
        Random random = new Random();

        Connection connection = new DatabaseConnection().get();

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
