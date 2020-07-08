package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import net.dv8tion.jda.api.entities.Activity;
import org.kilocraft.essentials.api.KiloServer;

import java.util.concurrent.TimeUnit;

public class OnlinePlayerUpdater extends Thread {
    private Activity.ActivityType type;

    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    public OnlinePlayerUpdater(Activity.ActivityType activityType) {
        type = activityType;
        setName("OnlinePlayer - Presence Updater");
    }

    @Override
    public void run() {
        while (true) {
            int rate = 1;

            
            int CurrentPlayers = KiloServer.getServer().getPlayerList().size();
            DiscordFab.getBot().setActivity(Activity.of(type, String.valueOf(CurrentPlayers) + " Players online!"));

            try {
                Thread.sleep(TimeUnit.MINUTES.toMillis(rate));
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }


        }
    }
}
