package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSynchronizer {
    private static final Logger LOGGER = DiscordFab.LOGGER;
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ShardManager BOT = DiscordFab.getBot();
    private static final Pattern LINK_KEY_PATTERN = Pattern.compile("(\\d{4})");

    public static boolean isLinkCode(@NotNull final String string) {
        Matcher matcher = LINK_KEY_PATTERN.matcher(string);
        return matcher.find();
    }

    public static int getLinkCode(@NotNull final String string) {
        Matcher matcher = LINK_KEY_PATTERN.matcher(string);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        return 0;
    }

    public static boolean isSynced(@NotNull final UUID uuid) {
        try {
            Connection conn = new DatabaseConnection().get();

            String selectSql = "SELECT McUUID FROM linkedaccounts WHERE McUUID = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong("DiscordId") != 0L;
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return false;
    }

    public static long getSyncedUserId(@NotNull final UUID uuid) {
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
        } catch (ClassNotFoundException | SQLException | NullPointerException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return 0L;
    }

    public static void sync(@NotNull final PrivateChannel channel, @NotNull final User user, final int inputLinkKey) {
        String selectSql = "SELECT LinkKey, DiscordId FROM linkedaccounts WHERE LinkKey = ?";
        try {
            Connection connection = new DatabaseConnection().get();

            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, inputLinkKey);

            ResultSet resultSet = selectStatement.executeQuery();
            resultSet.next();
            int linkKey = resultSet.getInt("LinkKey");
            long discordId = resultSet.getLong("DiscordId");

            if (inputLinkKey == linkKey && discordId != 0L) {
                String updateSql = "UPDATE linkedaccounts SET DiscordId = ? WHERE LinkKey = ?;";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setLong(1, user.getIdLong());
                updateStatement.setInt(2, linkKey);
                updateStatement.execute();

                channel.sendMessage("You were successfully linked!").queue();

                if (DISCORD_FAB.getConfig().userSync.syncDisplayName) {
                    syncDisplayName(linkKey);
                }
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Unexpected error while trying to sync user", e);
        }
    }

    private static void syncDisplayName(final int linkKey) throws SQLException, ClassNotFoundException {
        Connection conn = new DatabaseConnection().get();
        String selectSql = "SELECT DiscordId, McUsername FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setInt(1, linkKey);
        ResultSet resultSet = selectStmt.executeQuery();
        resultSet.next();
        final long discordId = resultSet.getLong("DiscordId");
        final String mcUsername = resultSet.getString("McUsername");

        Guild guild = DISCORD_FAB.getGuild();
        User user = BOT.getUserById(discordId);
        Role role = guild.getRoleById(DISCORD_FAB.getConfig().userSync.linkedRoleId);

        if (role == null) {
            LOGGER.warn("There is no Linked Role Selected!");
        } else if (user != null) {
            Member member = guild.getMember(user);
            if (member != null) {
                member.modifyNickname(mcUsername).complete();
                guild.addRoleToMember(member, role);
            }

        }

        conn.close();
    }
}
