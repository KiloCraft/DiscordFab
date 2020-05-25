package com.github.hansi132.DiscordFab.DiscordBot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class BotMain {
    public BotMain() throws LoginException {
        try {

            final Logger Logger = LoggerFactory.getLogger(BotMain.class);

            InetAddress address = InetAddress.getLocalHost();
            if (address.getHostAddress().equals("192.168.10.144")) {
                DefaultShardManagerBuilder
                        .create("", GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                        .addEventListeners(new Listener())
                        .setActivity(Activity.playing("Debugging"))
                        .build();
                Logger.info("Developer bot started.");
            } else {
                DefaultShardManagerBuilder
                        .create("", GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                        .addEventListeners(new Listener())
                        .setActivity(Activity.playing("20W19A @ mc.50kilo.org"))
                        .build();
                Logger.info("Production bot started");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }
}
