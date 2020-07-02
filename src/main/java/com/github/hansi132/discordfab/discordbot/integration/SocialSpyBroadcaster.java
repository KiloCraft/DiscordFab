package com.github.hansi132.discordfab.discordbot.integration;


import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerOnChatMessageEvent;

import java.util.HashSet;
import java.util.Set;

public class SocialSpyBroadcaster implements EventHandler<PlayerOnChatMessageEvent> {

    @Override
    public void handle(@NotNull PlayerOnChatMessageEvent event) {
        if (!event.getMessage().startsWith("/msg") || !event.getMessage().startsWith("/r")) {
            return;
        }

        //TODO Move this into a file, much code less efficient.
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


        String content = event.getMessage();

        for (String formats : format) {
            if (event.getMessage().contains(formats)) {
                content = event.getMessage().replace(formats, "");
            }
        }

        if (content.contains("@")) {
            content = content.replace("@", "");
        }

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(DiscordFab.getInstance().getDataConfig().getProperty("embedPicture"))
                .setUsername(event.getUser().getName())
                .setContent(content);
        WebhookMessage message = messageBuilder.build();

        WebhookClientBuilder clientBuilder = new WebhookClientBuilder(DiscordFab.getInstance().getDataConfig().getProperty("SocialSpyBroadcaster"));
        clientBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("SocialSpyBroadcaster");
            thread.setDaemon(true);
            return thread;
        });
        WebhookClient client = clientBuilder.build();

        client.send(message);
    }
}
