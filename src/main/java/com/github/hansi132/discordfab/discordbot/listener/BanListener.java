package com.github.hansi132.discordfab.discordbot.listener;

import com.github.hansi132.discordfab.DiscordFab;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.player.PlayerBannedEvent;

public class BanListener implements EventHandler<PlayerBannedEvent> {
    @Override
    public void handle(@NotNull PlayerBannedEvent PlayerBannedEvent) {
        DiscordFab.getInstance().getChatSynchronizer().onUserMute(PlayerBannedEvent.getVictim(), PlayerBannedEvent.getSource(), PlayerBannedEvent.getReason());
    }
}
