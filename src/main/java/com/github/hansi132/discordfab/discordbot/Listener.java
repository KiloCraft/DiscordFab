package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import com.github.hansi132.discordfab.discordbot.integration.AssignNick;
import com.github.hansi132.discordfab.discordbot.integration.McBroadcaster;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.sql.*;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    Connection connection;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        //Reaction event handler. Will handle all events where the reaction is added.
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        String db = new DataConfig().getProperty("database");
        String dbUser = new DataConfig().getProperty("databaseUser");
        String dbPassword = new DataConfig().getProperty("databasePassword");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(db, dbUser, dbPassword);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
            String selectSql = "SELECT LinkKey, DiscordId FROM linkedaccounts WHERE LinkKey = ?";
            try {
                int message = Integer.parseInt(event.getMessage().getContentRaw());
                PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
                preparedStatement.setInt(1, message);

                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                int linkKey = resultSet.getInt("LinkKey");
                String DiscordId = resultSet.getString("DiscordId");

                if (message == linkKey && DiscordId == null) {
                    String updateSql = "UPDATE linkedaccounts SET DiscordId = ? WHERE LinkKey = ?;";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setString(1, event.getAuthor().getId());
                    updateStatement.setInt(2, linkKey);
                    updateStatement.execute();

                    event.getPrivateChannel().sendMessage("You were linked.").queue();
                    new AssignNick(linkKey);

                }

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        if (!user.isBot()) {
            //Basic broadcast function to send messages discord->MC
            if (new DataConfig().getProperty("broadcastEnable").equals("true")) {
                new McBroadcaster(event);
            }

            String prefix = "k!";
            String raw = event.getMessage().getContentRaw();

            if (raw.startsWith(prefix)) {
                BotCommandSource src = new BotCommandSource(
                        event.getJDA(), user.getName(), event.getGuild(), event.getChannel(), user, event.getMember(), event
                );

                DiscordFab.getInstance().getCommandManager().execute(src, raw.replaceFirst("k!", ""));
            }
        }
    }
}
