package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
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
    private final CommandManager Manager = new CommandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if (event.isFromType(ChannelType.PRIVATE)) {
            WebhookMessageBuilder builder = new WebhookMessageBuilder()
                    .setUsername(event.getAuthor().getName())
                    .setAvatarUrl(event.getAuthor().getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"))
                    .setContent(event.getMessage().getContentRaw());
            new Webhook(builder);
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();


        if (event.getJDA().getSelfUser().getAsTag().equals("HansiPlaysBotDev#1196")) {
            String prefix = "dk!";
            String raw = event.getMessage().getContentRaw();
            if (raw.startsWith(prefix)) {
                //Here we send the command so we can handle it.

            }
        } else {
            String prefix = "k!";
            String raw = event.getMessage().getContentRaw();
            if (raw.startsWith(prefix)) {
                //Here we send the command so we can handle it.
                Manager.handle(event);
            }
        }
    }
}


