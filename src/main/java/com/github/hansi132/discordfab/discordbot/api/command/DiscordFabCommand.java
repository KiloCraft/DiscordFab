package com.github.hansi132.discordfab.discordbot.api.command;

import com.github.hansi132.discordfab.DiscordFab;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

public abstract class DiscordFabCommand {
    protected static final transient int SUCCESS = 1;
    protected static final transient int FAILED = -1;
    protected static final transient int AWAIT = 0;
    protected static final transient DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    protected static final transient Logger LOGGER = LogManager.getLogger();

    private final transient String label;
    private transient String[] alias;
    protected transient LiteralArgumentBuilder<BotCommandSource> argBuilder;
    protected transient LiteralCommandNode<BotCommandSource> cmdNode;

    public DiscordFabCommand(@NotNull final String label) {
        this.label = label;
    }

    public DiscordFabCommand(@NotNull final String label, @NotNull final String... alias) {
        this.label = label;
        this.alias = alias;
    }

    public static LiteralArgumentBuilder<BotCommandSource> literal(@NotNull final String label) {
        return LiteralArgumentBuilder.literal(label);
    }

    public static <T> RequiredArgumentBuilder<BotCommandSource, T> argument(@NotNull final String label,
                                                                            @NotNull final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(label, type);
    }
}
