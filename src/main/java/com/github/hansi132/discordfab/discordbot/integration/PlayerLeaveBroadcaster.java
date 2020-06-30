package com.github.hansi132.discordfab.discordbot.integration;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerDisconnectEvent;

public class PlayerLeaveBroadcaster implements EventHandler<PlayerDisconnectEvent> {

    @Override
    public void handle(@NotNull PlayerDisconnectEvent playerDisconnectEvent) {

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(new DataConfig().getProperty("embedPicture"))
                .setUsername("KiloCord")
                .setContent("- " + playerDisconnectEvent.getPlayer().getName().asString());
        WebhookMessage message = messageBuilder.build();

        WebhookClientBuilder clientBuilder = new WebhookClientBuilder(new DataConfig().getProperty("discordBroadcaster"));
        clientBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("discordLeaveThread");
            thread.setDaemon(true);
            return thread;
        });
        WebhookClient client = clientBuilder.build();

        client.send(message);
    }

}
