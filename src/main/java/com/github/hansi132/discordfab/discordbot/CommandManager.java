package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class CommandManager {
    private final Logger LOGGER = LogManager.getLogger();
    private final Map<String, DiscordFabCommand> commands;
    private final CommandDispatcher<BotCommandSource> dispatcher;

    public CommandManager(@NotNull final DiscordFab discordFab) {
        this.commands = Maps.newHashMap();
        this.dispatcher = new CommandDispatcher<>();

    }

    public <C extends DiscordFabCommand> void register(C command) {
        this.commands.put(command.getLabel().toLowerCase(Locale.ROOT), command.register(this.dispatcher));
    }

    @Nullable
    public DiscordFabCommand getCommand(final String label) {
        return this.commands.get(label.toLowerCase(Locale.ROOT));
    }

    public Map<String, DiscordFabCommand> getCommands() {
        return this.commands;
    }

    public int execute(@NotNull final BotCommandSource src, @NotNull final String input) {
        byte i = 0;
        try {
            i = (byte) this.dispatcher.execute(input, src);
        } catch (CommandSyntaxException e) {
            src.sendFeedback(e.getMessage());
        }

        return i;
    }
}
