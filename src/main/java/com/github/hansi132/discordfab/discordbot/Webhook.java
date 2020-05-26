package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;

public class Webhook {

    private final WebhookClient client;

    public Webhook(WebhookMessageBuilder message) {
        WebhookClientBuilder builder = new WebhookClientBuilder("https://discordapp.com/api/webhooks/714607787858198604/MLHpyWGacIInJ396HVtu17kn9UN_GxSGqLGkJJn457zIScMAb0uQOKQbfDGxwTcebZ4r");
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Hello");
            thread.setDaemon(true);
            return thread;
        });

        this.client = builder.build();
        client.send(message.build());
    }
}
