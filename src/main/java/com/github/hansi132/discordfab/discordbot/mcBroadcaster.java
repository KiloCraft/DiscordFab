package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;

public class mcBroadcaster {

    public mcBroadcaster(GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(new DataConfig().getProperty("broadcast"))) {
            TextMessage text = new TextMessage(event.getAuthor().getAsTag() + ": " + event.getMessage().getContentDisplay());
            KiloChat.broadCast(text);
        }
    }
}
