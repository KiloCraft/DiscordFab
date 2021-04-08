package com.github.hansi132.discordfab.discordbot.listener;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.event.EventHandler;
import org.kilocraft.essentials.api.event.server.ServerTickEvent;

import java.sql.SQLException;

public class TickListener implements EventHandler<ServerTickEvent> {
    @Override
    public void handle(@NotNull ServerTickEvent serverTickEvent) {
        if (serverTickEvent.getServer().getTicks() % 100 == 0) {
            try {
                UserSynchronizer.clearMuteRole();
            } catch (SQLException throwables) {
                DiscordFab.LOGGER.error("An error occurred while trying to removed muted roles: ", throwables);
            }
        }
    }
}
