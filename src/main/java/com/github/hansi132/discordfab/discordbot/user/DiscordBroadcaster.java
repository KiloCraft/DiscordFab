package com.github.hansi132.discordfab.discordbot.user;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessage;
import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.server.lifecycle.ServerStopEvent;

public class DiscordBroadcaster {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private final WebhookClient client;

    public DiscordBroadcaster() {
        WebhookClientBuilder builder = new WebhookClientBuilder(DISCORD_FAB.getDataConfig().getProperty("discordBroadcaster"));
        builder.setAllowedMentions(AllowedMentions.none());
        this.client = builder.build();

        KiloEssentials.getServer().registerEvent((EventHandler<ServerStopEvent>) event -> this.client.close());
    }

    public void send(@NotNull WebhookMessage message) {
        this.client.send(message);
    }

    public WebhookClient getClient() {
        return this.client;
    }
}
