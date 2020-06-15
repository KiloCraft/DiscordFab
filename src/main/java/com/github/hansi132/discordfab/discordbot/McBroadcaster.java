package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.TextMessage;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class McBroadcaster {

    public McBroadcaster(GuildMessageReceivedEvent event) {
        if (event.getChannel().getId().equals(new DataConfig().getProperty("broadcastChannel"))) {
            String colorPrefix = "&7";
            String prefix = "Guest";
            String message = event.getMessage().getContentDisplay();
            String user = event.getAuthor().getAsTag();
            boolean sendColor = false;


            switch (Objects.requireNonNull(event.getMember()).getRoles().get(0).getName()) {
                case "Owner":
                    colorPrefix = "&4";
                    prefix = "Owner";
                    sendColor = true;
                    break;
                case "Admin":
                    colorPrefix = "&c";
                    prefix = "A";
                    sendColor = true;
                    break;
                case "Developer":
                    colorPrefix = "&e";
                    prefix = "Dev";
                    sendColor = true;
                    break;
                case "Moderator":
                    colorPrefix = "&9";
                    prefix = "Mod";
                    sendColor = true;
                    break;
                case "Helper":
                    colorPrefix = "&2";
                    prefix = "H";
                    sendColor = true;
                    break;
                case "Builder":
                    colorPrefix = "&a";
                    prefix = "B";
                    sendColor = true;
                    break;
                case "Kilocrafter+":
                    colorPrefix = "&6";
                    prefix = "KC+";
                    sendColor = true;
                    break;
                case "Kilocrafter":
                    colorPrefix = "&6";
                    prefix = "KC";
                    sendColor = true;
                    break;
                case "Snapshot Ultimate":
                    colorPrefix = "&d";
                    prefix = "U";
                    sendColor = true;
                    break;
                case "Member":
                    colorPrefix = "&b";
                    prefix = "M";
                    sendColor = true;
                    break;
                case "Player+":
                    colorPrefix = "&3";
                    prefix = "P+";
                    break;
                case "Player":
                    colorPrefix = "&3";
                    prefix = "P";
                    break;
                case "Donator":
                    colorPrefix = "&5";
                    prefix = "D";
                    sendColor = true;
                    break;
                default:
                    colorPrefix = "&7";
                    prefix = "G";
            }

            Set<String> format = new HashSet<>();
            format.add("&0");
            format.add("&1");
            format.add("&2");
            format.add("&3");
            format.add("&4");
            format.add("&5");
            format.add("&6");
            format.add("&7");
            format.add("&8");
            format.add("&9");
            format.add("&a");
            format.add("&b");
            format.add("&c");
            format.add("&d");
            format.add("&e");
            format.add("&f");
            format.add("&l");
            format.add("&o");
            format.add("&n");
            format.add("&m");
            format.add("&k");
            format.add("&r");

            if (!sendColor) {
                for (String formats : format) {
                    if (message.contains(formats)) {
                        message = message.replace(formats, "");
                    }
                }
            }

            TextMessage text = new TextMessage("&f[" + colorPrefix + prefix + "&f] "+ colorPrefix + user + " &8>&7> " + "&f" + message);
            KiloChat.broadCast(text);
        }
    }
}
