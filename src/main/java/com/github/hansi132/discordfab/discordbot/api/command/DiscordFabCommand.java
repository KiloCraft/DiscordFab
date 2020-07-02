package com.github.hansi132.discordfab.discordbot.api.command;

import com.github.hansi132.discordfab.DiscordFab;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private @Nullable transient String description;

    public DiscordFabCommand(@NotNull final String label) {
        this(label, (String[]) null);
    }

    public DiscordFabCommand(@NotNull final String label, @Nullable final String... alias) {
        this.label = label;
        this.alias = alias;
        this.argBuilder = literal(label);
        this.cmdNode = this.argBuilder.build();
    }

    public static LiteralArgumentBuilder<BotCommandSource> literal(@NotNull final String label) {
        return LiteralArgumentBuilder.literal(label);
    }

    public static <T> RequiredArgumentBuilder<BotCommandSource, T> argument(@NotNull final String label,
                                                                            @NotNull final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(label, type);
    }

    public String getLabel() {
        return this.label;
    }

    @Nullable
    public String getDescription() {
        return this.description;
    }

    protected void withDescription(@Nullable final String description) {
        this.description = description;
    }

    public DiscordFabCommand register(@NotNull final CommandDispatcher<BotCommandSource> dispatcher) {
        dispatcher.register(argBuilder);
        dispatcher.getRoot().addChild(cmdNode);

        if (this.alias != null) {
            for (String s : this.alias) {
                LiteralArgumentBuilder<BotCommandSource> builder = literal(s).requires(this.argBuilder.getRequirement());

                if (this.argBuilder.getCommand() != null) {
                    builder.executes(this.argBuilder.getCommand());
                }

                dispatcher.register(builder);
            }
        }

        return this;
    }
}
