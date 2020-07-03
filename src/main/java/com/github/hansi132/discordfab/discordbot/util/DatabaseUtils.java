package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseUtils {

    public static boolean isLinked(@NotNull final UUID uuid) {
        try {
            Connection conn = new DatabaseConnection().get();

            String selectSql = "SELECT McUUID FROM linkedaccounts WHERE McUUID = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return false;
    }

    public static long getLinkedUserId(@NotNull final UUID uuid) {
        try {
            Connection conn = new DatabaseConnection().get();

            String selectSql = "SELECT DiscordId FROM linkedaccounts WHERE McUUID = ?";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("DiscordId");
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return 0L;
    }
}
