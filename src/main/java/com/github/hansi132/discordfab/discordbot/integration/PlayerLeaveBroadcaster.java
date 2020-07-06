package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerDisconnectEvent;

public class PlayerLeaveBroadcaster implements EventHandler<PlayerDisconnectEvent> {

    @Override
    public void handle(@NotNull PlayerDisconnectEvent playerDisconnectEvent) {
        DiscordFab.getInstance().getChatSynchronizer().onUserLeave(playerDisconnectEvent.getUser());
    }

}
