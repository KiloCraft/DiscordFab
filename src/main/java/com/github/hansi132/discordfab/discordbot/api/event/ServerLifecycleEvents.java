package com.github.hansi132.discordfab.discordbot.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

public final class ServerLifecycleEvents {
    public static final Event<ServerStarted> SERVER_STARTED = EventFactory.createArrayBacked(ServerStarted.class, (callbacks) -> (server) -> {
        ServerStarted[] var2 = callbacks;
        int var3 = callbacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ServerStarted callback = var2[var4];
            callback.onServerStarted(server);
        }

    });
    public static final Event<ServerStopping> SERVER_STOPPING = EventFactory.createArrayBacked(ServerStopping.class, (callbacks) -> (server) -> {
        ServerStopping[] var2 = callbacks;
        int var3 = callbacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            ServerStopping callback = var2[var4];
            callback.onServerStopping(server);
        }

    });


    public interface ServerStopping {
        void onServerStopping(MinecraftServer var1);
    }

    public interface ServerStarted {
        void onServerStarted(MinecraftServer var1);
    }
}
