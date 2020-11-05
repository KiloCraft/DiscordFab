package com.github.hansi132.discordfab.discordbot.api.event;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.command.ServerCommandSource;

public interface CommandRegistrationCallback {

    Event<CommandRegistrationCallback> EVENT = EventFactory.createArrayBacked(CommandRegistrationCallback.class, (callbacks) -> (dispatcher, dedicated) -> {
        for (CommandRegistrationCallback callback : callbacks) {
            callback.register(dispatcher, dedicated);
        }
    });

    void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated);

}
