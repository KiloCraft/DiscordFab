package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.query.QueryOptions;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.api.user.OnlineUser;

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

    public static boolean isLinked(@NotNull final UUID uuid) {
        try {
            Connection conn = DatabaseConnection.connect();

            String selectSql = "SELECT McUUID, DiscordId FROM linkedaccounts WHERE McUUID = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setString(1, uuid.toString());
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                long discordID = resultSet.getLong("DiscordId");
                conn.close();
                return discordID != 0L;
            }

            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return false;
    }

    public static boolean isLinked(final long discordId) {
        try {
            Connection conn = DatabaseConnection.connect();

            String selectSql = "SELECT McUUID, DiscordId FROM linkedaccounts WHERE DiscordId = ?;";
            PreparedStatement selectStatement = conn.prepareStatement(selectSql);
            selectStatement.setLong(1, discordId);
            ResultSet resultSet = selectStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString("McUUID") != null;
            }

            conn.close();
        } catch (SQLException | ClassNotFoundException e) {
            DiscordFab.LOGGER.error("Could not query the database!", e);
        }

        return false;
    }

    public static long getSyncedUserId(@NotNull final UUID uuid) {
        try {
            Connection conn = DatabaseConnection.connect();

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

    public static void sync(@NotNull final PrivateChannel channel, @NotNull final User user, final int linkKey) {
        String selectSql = "SELECT McUUID FROM linkedaccounts WHERE LinkKey = ? AND DiscordID IS NULL";
        try {
            Connection connection = DatabaseConnection.connect();

            PreparedStatement selectStatement = connection.prepareStatement(selectSql);
            selectStatement.setInt(1, linkKey);

            ResultSet resultSet = selectStatement.executeQuery();
            if (resultSet.next()) {
                String mcUUID = resultSet.getString("McUUID");
                String updateSql = "UPDATE linkedaccounts SET DiscordId = ? WHERE LinkKey = ?;";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setLong(1, user.getIdLong());
                updateStatement.setInt(2, linkKey);
                updateStatement.execute();

                OnlineUser onlineUser = KiloServer.getServer().getOnlineUser(UUID.fromString(mcUUID));
                channel.sendMessage(
                        DISCORD_FAB.getConfig().messages.successfully_linked
                                .replace("%player%", onlineUser == null ? mcUUID : onlineUser.getName())
                ).queue();
                if (onlineUser != null) {
                    KiloEssentials.getServer().execute(DISCORD_FAB.getConfig().userSync.command
                            .replace("%player%", onlineUser.getName()));
                }

                if (DISCORD_FAB.getConfig().userSync.syncDisplayName) {
                    syncDisplayName(linkKey);
                }
                syncRoles(UUID.fromString(mcUUID));
            } else {
                channel.sendMessage(DISCORD_FAB.getConfig().messages.invalid_link_key).queue();
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Unexpected error while trying to sync user", e);
        }
    }

    @Deprecated
    public static void syncRoles(final int linkKey) throws SQLException, ClassNotFoundException {
        Connection conn = DatabaseConnection.connect();
        String selectSql = "SELECT DiscordId, McUUID FROM linkedaccounts WHERE LinkKey = ?;";
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setInt(1, linkKey);
        ResultSet resultSet = selectStmt.executeQuery();
        resultSet.next();
        final long discordId = resultSet.getLong("DiscordId");
        final UUID mcUUID = UUID.fromString(resultSet.getString("McUUID"));
        syncRoles(discordId, mcUUID);
    }

    public static void syncRoles(final UUID mcUUID) throws SQLException, ClassNotFoundException {
        Connection conn = DatabaseConnection.connect();
        String selectSql = "SELECT DiscordId FROM linkedaccounts WHERE McUUID = ?;";
        PreparedStatement selectStmt = conn.prepareStatement(selectSql);
        selectStmt.setString(1, mcUUID.toString());
        ResultSet resultSet = selectStmt.executeQuery();
        if (resultSet.next()) {
            final long discordId = resultSet.getLong("DiscordId");
            if (discordId != 0) {
                syncRoles(discordId, mcUUID);
            }
        }
    }

    private static void syncRoles(final long discordID, final UUID mcUUID) {
        Guild guild = DISCORD_FAB.getGuild();
        BOT.getRoles();
        for (Role role : guild.getRoles()) {
            long roleID = role.getIdLong();
            if (shouldSync(mcUUID, roleID)) {
                User user = BOT.getUserById(discordID);
                if (user != null) {
                    Member member = guild.getMember(user);
                    if (member != null) {
                        try {
                            guild.addRoleToMember(member, role).queue();
                        } catch (HierarchyException ignored) {
                        }
                    }
                }
            }
        }
    }

    private static boolean shouldSync(UUID uuid, long roleID) {
        final String SYNC_PERM_PREFIX = "discordfab.sync.";
        LuckPerms luckPerms = LuckPermsProvider.get();
        net.luckperms.api.model.user.User user = luckPerms.getUserManager().getUser(uuid);
        if (user != null) {
            QueryOptions options = luckPerms.getContextManager().getStaticQueryOptions();
            return user.getCachedData().getPermissionData(options).checkPermission(SYNC_PERM_PREFIX + roleID).asBoolean();
        }
        return false;
    }

    private static void syncDisplayName(final int linkKey) throws SQLException, ClassNotFoundException {
        Connection conn = DatabaseConnection.connect();
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
                guild.addRoleToMember(member, role).queue();
            }

        }

        conn.close();
    }
}
