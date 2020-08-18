package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import net.dv8tion.jda.api.entities.Activity;
import org.kilocraft.essentials.api.KiloServer;

import java.util.concurrent.TimeUnit;

public class OnlinePlayerUpdater extends Thread {
    private final Activity.ActivityType type;

    public OnlinePlayerUpdater(Activity.ActivityType activityType) {
        type = activityType;
        this.setName("OnlinePlayer - Presence Updater");
    }

    @Override
    public void run() {
        while (true) {
            int rate = 1;
            int currentPlayers = KiloServer.getServer().getPlayerList().size();
            DiscordFab.getBot().setActivity(Activity.of(type, currentPlayers + " Players online!"));

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(rate));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
