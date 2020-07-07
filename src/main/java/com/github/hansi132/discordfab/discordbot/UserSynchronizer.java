package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSynchronizer {
    private static final Logger LOGGER = DiscordFab.LOGGER;
    private static final Pattern LINK_KEY_PATTERN = Pattern.compile("(^\\d{4}$)");

    public static boolean isLinkCode(@NotNull final String string) {
        Matcher matcher = LINK_KEY_PATTERN.matcher(string);
        return matcher.find();
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
//                new AssignNick(linkKey);
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Unexpected error while trying to sync user", e);
        }
    }
}
