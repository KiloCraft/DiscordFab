package com.github.hansi132.discordfab.discordbot.listener;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerOnChatMessageEvent;

public class ChatMessageListener implements EventHandler<PlayerOnChatMessageEvent> {
    @Override
    public void handle(@NotNull PlayerOnChatMessageEvent event) {
        if (DiscordFab.getInstance().getConfig().chatSynchronizer.toDiscord) {
            DiscordFab.getInstance().getChatSynchronizer().onGameChat(event.getChannel(), event.getUser(), event.getMessage());
        }
    }
}
