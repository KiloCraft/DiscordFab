package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;

import java.util.Objects;

public class McBroadcaster {

    public McBroadcaster(GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(new DataConfig().getProperty("broadcast"))) {
            String colorPrefix = "&7";
            String prefix = "Guest";


            switch (Objects.requireNonNull(event.getMember()).getRoles().get(0).getName()) {
                case "Owner":
                    colorPrefix = "&4";
                    prefix = "Owner";
                    break;
                case "Admin":
                    colorPrefix = "&c";
                    prefix = "A";
                    break;
                case "Developer":
                    colorPrefix = "&e";
                    prefix = "Dev";
                    break;
                case "Moderator":
                    colorPrefix = "&9";
                    prefix = "Mod";
                    break;
                case "Helper":
                    colorPrefix = "&2";
                    prefix = "H";
                    break;
                case "Builder":
                    colorPrefix = "&a";
                    prefix = "B";
                    break;
                case "Kilocrafter":
                    colorPrefix = "&6";
                    prefix = "KC";
                    break;
                case "Snapshot Ultimate":
                    colorPrefix = "&d";
                    prefix = "U";
                    break;
                case "Member":
                    colorPrefix = "&b";
                    prefix = "M";
                    break;
                case "[+]":
                    colorPrefix = "&3";
                    prefix = "P+";
                    break;
                case "Donator":
                    colorPrefix = "&5";
                    prefix = "D";
                    break;
                default:
                    colorPrefix = "&7";
                    prefix = "G";
            }

            System.out.println(Objects.requireNonNull(event.getMember()).getColor());

            TextMessage text = new TextMessage("&f[" + colorPrefix + prefix + "&f] "+ colorPrefix + event.getAuthor().getAsTag() + " &8>&7> " + "&f" + event.getMessage().getContentDisplay());
            KiloChat.broadCast(text);
        }
    }
}
