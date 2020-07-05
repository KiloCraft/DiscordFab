package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.util.DatabaseConnection;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
        try {
            Connection connection = new DatabaseConnection().get();
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.fatal("Could not connect to the Database!", e);
        }
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        final String raw = event.getMessage().getContentRaw();
        if (event.isFromType(ChannelType.PRIVATE) && !event.getAuthor().isBot() && UserSynchronizer.isLinkCode(raw)) {
            int code = Integer.parseInt(raw);
            UserSynchronizer.sync(event.getPrivateChannel(), event.getAuthor(), code);
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        final User user = event.getAuthor();
        if (user.isBot()) {
            return;
        }

        final String raw = event.getMessage().getContentRaw();
        final String prefix = DiscordFab.getInstance().getConfig().prefix;

        if (!event.isWebhookMessage() && !raw.equals(prefix) && raw.startsWith(prefix)) {
            final BotCommandSource src = new BotCommandSource(
                    event.getJDA(), event.getGuild(), event.getChannel(), user, event.getMember(), event
            );

            DISCORD_FAB.getCommandManager().execute(src, raw);
        } else if (!event.isWebhookMessage() && DISCORD_FAB.getConfig().chatSynchronizer.to_minecraft) {
            if (event.getChannel().getIdLong() == DISCORD_FAB.getConfig().chatSynchronizer.chat_channel_id) {
                DiscordFab.getInstance().getChatSynchronizer().onDiscordChat(Objects.requireNonNull(event.getMember()), raw);
            }
        }
    }
}
