package com.github.hansi132.DiscordFab.DiscordBot;

import com.github.hansi132.DiscordFab.DiscordBot.Listener;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.*;

import javax.security.auth.login.LoginException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class botMain {
    public botMain() throws LoginException {
        try {

            final Logger Logger = LoggerFactory.getLogger(botMain.class);

            InetAddress adress = InetAddress.getLocalHost();
            if(adress.getHostAddress().equals("192.168.10.144")) {
                System.out.println(adress.getHostAddress());
                DefaultShardManagerBuilder
                        .create("NjY0MTI0Mzc0MDM4NjA5OTIx.Xr0wTw.JEZZYCoActY8EZOnGxbIoMZNK8I", GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
                        .addEventListeners(new Listener())
                        .setActivity(Activity.playing("Debugging"))
                        .build();
                Logger.info("Developer bot started.");
            } else {
                DefaultShardManagerBuilder
                        .create("NjY0MTI0Mzc0MDM4NjA5OTIx.Xr0wTw.JEZZYCoActY8EZOnGxbIoMZNK8I", GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS))
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
