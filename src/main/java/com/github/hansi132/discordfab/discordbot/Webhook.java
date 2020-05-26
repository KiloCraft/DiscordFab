package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.JDA;

import javax.xml.crypto.Data;

public class Webhook {

    private final WebhookClient client;

    public Webhook(WebhookMessageBuilder message) {
        WebhookClientBuilder builder = new WebhookClientBuilder(new DataConfig().getProperty("webhook_url"));
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("WebhookThread");
            thread.setDaemon(true);
            return thread;
        });

        this.client = builder.build();
        client.send(message.build());
    }
}
