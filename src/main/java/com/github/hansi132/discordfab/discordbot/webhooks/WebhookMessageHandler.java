package com.github.hansi132.discordfab.discordbot.webhooks;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WebhookMessageHandler {

    public WebhookMessageHandler(MessageReceivedEvent event) {
        //Building the message
        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setUsername(event.getAuthor().getName())
                .setAvatarUrl(event.getAuthor().getEffectiveAvatarUrl().replaceFirst("gif", "png" + "?size=512"))
                .setContent(event.getMessage().getContentRaw());
        WebhookMessage message = messageBuilder.build();

        //Building the client
        WebhookClientBuilder clientBuilder = new WebhookClientBuilder(new DataConfig().getProperty("webhook_url"));
        clientBuilder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("WebhookThread");
            thread.setDaemon(true);
            return thread;
        });
        WebhookClient client = clientBuilder.build();

        //Message thru the client
        client.send(message);
    }
}
