package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.webhooks.WebhookMessageHandler;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();
    private final CommandManagerOLD Manager = new CommandManagerOLD();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            new WebhookMessageHandler(event);
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        String prefix = "k!";
        String raw = event.getMessage().getContentRaw();
        if (raw.startsWith(prefix)) {
            BotCommandSource src = new BotCommandSource(
                    event.getJDA(), user.getName(), event.getGuild(), event.getChannel(), user, event.getMember(), event
            );

            DiscordFab.getInstance().getCommandManager().execute(src, raw);
        }
    }

    //    @Override
//    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
//        User user = event.getAuthor();
//
//        if (event.getJDA().getSelfUser().getAsTag().equals("HansiPlaysBotDev#1196")) {
//            String prefix = "dk!";
//            String raw = event.getMessage().getContentRaw();
//            if (raw.startsWith(prefix)) {
//                //Here we send the command so we can handle it.
//
//            }
//        } else {
//            String prefix = "k!";
//            String raw = event.getMessage().getContentRaw();
//            if (raw.startsWith(prefix)) {
//                //Here we send the command so we can handle it.
//                Manager.handle(event);
//            }
//        }
//    }
}
