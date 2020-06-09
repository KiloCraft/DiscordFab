package com.github.hansi132.discordfab.discordbot;



import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.discordbot.config.DataConfig;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerOnChatMessageEvent;

public class DiscordBroadcaster implements EventHandler<PlayerOnChatMessageEvent> {

    @Override
    public void handle(@NotNull PlayerOnChatMessageEvent event) {
        if (event.getMessage().startsWith("/")) {
            return;
        }

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setUsername(event.getPlayer().getName().asString())
                .setContent(event.getMessage());
        WebhookMessage message = messageBuilder.build();

        WebhookClientBuilder clientBuilder = new WebhookClientBuilder(new DataConfig().getProperty("discordbroadcaster"));
        clientBuilder.setThreadFactory((job) -> {
           Thread thread = new Thread(job);
           thread.setName("discordBroadcasterThread");
           thread.setDaemon(true);
           return thread;
        });
        WebhookClient client = clientBuilder.build();

        client.send(message);

        //Objects.requireNonNull(DiscordFab.getBot().getTextChannelById("710847688068825178")).sendMessage(event.getMessage()).queue();
    }
}
