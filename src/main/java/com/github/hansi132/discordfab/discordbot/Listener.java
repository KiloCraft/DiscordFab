package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    Connection connection;

    @Override
    public void onMessageReactionAdd(@Nonnull MessageReactionAddEvent event) {
        //Reaction event handler. Will handle all events where the reaction is added.
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        try {
            connection = new DatabaseConnection().get();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot()) {
            String selectSql = "SELECT LinkKey, DiscordId FROM linkedaccounts WHERE LinkKey = ?";
            try {
                int message = Integer.parseInt(event.getMessage().getContentRaw());
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, message);

                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                int linkKey = resultSet.getInt("LinkKey");
                String DiscordId = resultSet.getString("DiscordId");

                if (message == linkKey && DiscordId == null) {
                    String updateSql = "UPDATE linkedaccounts SET DiscordId = ? WHERE LinkKey = ?;";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setLong(1, event.getAuthor().getIdLong());
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
            if (DISCORD_FAB.getDataConfig().getProperty("broadcastEnable").equals("true")) {
                new McBroadcaster(event);
            }

            String prefix = DiscordFab.getInstance().getConfig().prefix;
            String raw = event.getMessage().getContentRaw();

            if (raw.startsWith(prefix)) {
                BotCommandSource src = new BotCommandSource(
                        event.getJDA(), user.getName(), event.getGuild(), event.getChannel(), user, event.getMember(), event
                );

                DISCORD_FAB.getCommandManager().execute(
                        src,
                        raw.replaceFirst(DiscordFab.getInstance().getConfig().prefix, "")
                );
            }
        }
    }
}
