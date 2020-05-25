package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;

import java.util.Map;

public class CommandManager {
    private final Map<String, DiscordFabCommand> commands = Maps.newHashMap();
    private final CommandDispatcher<?> dispatcher = new CommandDispatcher<>();

    public CommandManager() {

    }

}
