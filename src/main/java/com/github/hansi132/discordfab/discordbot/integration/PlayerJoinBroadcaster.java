package com.github.hansi132.discordfab.discordbot.integration;


import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerConnectedEvent;

public class PlayerJoinBroadcaster implements EventHandler<PlayerConnectedEvent> {

    @Override
    public void handle(@NotNull PlayerConnectedEvent playerConnectedEvent) {

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(new DataConfig().getProperty("embedPicture"))
                .setUsername("KiloCord")
                .setContent("+ " + playerConnectedEvent.getUser().getName());
        WebhookMessage message = messageBuilder.build();

        WebhookClientBuilder clientBuilder = new WebhookClientBuilder(new DataConfig().getProperty("discordBroadcaster"));
        clientBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("discordJoinThread");
            thread.setDaemon(true);
            return thread;
        });
        WebhookClient client = clientBuilder.build();

        client.send(message);
    }
}
