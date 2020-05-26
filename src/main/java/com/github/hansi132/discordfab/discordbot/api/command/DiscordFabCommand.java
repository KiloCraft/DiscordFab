package com.github.hansi132.discordfab.discordbot.api.command;

import org.jetbrains.annotations.NotNull;

public abstract class DiscordFabCommand {
    private final transient String label;
    private transient String[] alias;

    public DiscordFabCommand(@NotNull final String label) {
        this.label = label;
    }

    public DiscordFabCommand(@NotNull final String label, @NotNull final String... alias) {
        this.label = label;
        this.alias = alias;
    }
}
