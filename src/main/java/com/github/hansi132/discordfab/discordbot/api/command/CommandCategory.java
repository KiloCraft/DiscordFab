package com.github.hansi132.discordfab.discordbot.api.command;

public enum CommandCategory {
    DEBUG("Debug"),
    GENERAL("General"),
    ADMIN("Admin"),
    UTILITY("Utility"),
    HELP("Help"),
    FUN("Fun");

    private final String name;
    CommandCategory(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
