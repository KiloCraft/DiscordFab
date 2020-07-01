package com.github.hansi132.discordfab.discordbot.integration;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerConnectedEvent;

public class PlayerJoinBroadcaster implements EventHandler<PlayerConnectedEvent> {

    @Override
    public void handle(@NotNull PlayerConnectedEvent playerConnectedEvent) {
        DiscordFab.getInstance().getChatSynchronizer().onUserJoin(playerConnectedEvent.getUser());
    }
}
