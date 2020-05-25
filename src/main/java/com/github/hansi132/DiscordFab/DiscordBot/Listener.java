package com.github.hansi132.DiscordFab.DiscordBot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

public class Listener extends ListenerAdapter {
    final Logger Logger = LoggerFactory.getLogger(Listener.class);
    private final commandManager Manager = new commandManager();

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        Logger.info("{} is ready", event.getJDA().getSelfUser().getAsTag());
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

        if(event.getJDA().getSelfUser().getAsTag().equals("HansiPlaysBotDev#1196")) {
            String prefix = "dk!";
            String raw = event.getMessage().getContentRaw();
            if (raw.startsWith(prefix)) {
                Manager.handle(event);
            }
        }
        else {
            String prefix = "k!";
            String raw = event.getMessage().getContentRaw();
            if(raw.startsWith(prefix)){
                Manager.handle(event);
            }
        }
    }
}


