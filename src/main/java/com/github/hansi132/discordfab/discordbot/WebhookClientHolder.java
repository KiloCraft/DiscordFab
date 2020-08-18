package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessage;
import com.google.common.collect.Maps;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class WebhookClientHolder {
    private final Map<String, WebhookClient> clients;

    public WebhookClientHolder() {
        this.clients = Maps.newHashMap();
    }

    public void addClient(@NotNull final String id, @NotNull final String url) {
        addClient(id, new WebhookClientBuilder(url).setAllowedMentions(AllowedMentions.none()).build());
    }

    public void addClient(@NotNull final String id, @NotNull final WebhookClient client) {
        this.clients.put(id, client);
    }

    @Nullable
    public WebhookClient getClient(@NotNull final String id) {
        return this.clients.get(id);
    }

    public void send(@NotNull final String clientId, @NotNull final WebhookMessage message) {
        final WebhookClient client = this.getClient(clientId);
        if (client != null) {
            client.send(message);
        }
    }

    void closeAll() {
        this.clients.forEach((id, client) -> client.close());
        this.clients.clear();
    }

}
