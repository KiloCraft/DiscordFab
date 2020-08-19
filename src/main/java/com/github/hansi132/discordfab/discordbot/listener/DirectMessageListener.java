package com.github.hansi132.discordfab.discordbot.listener;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerOnDirectMessageEvent;

public class DirectMessageListener implements EventHandler<PlayerOnDirectMessageEvent> {
    @Override
    public void handle(@NotNull PlayerOnDirectMessageEvent event) {
        DiscordFab.getInstance().getChatSynchronizer().onGameDirectChat(event.getSource(), event.getReceiver(), event.getMessage());
    }
}
