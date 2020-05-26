package com.github.hansi132.discordfab.discordbot.api.text;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public enum Format {
    NORMAL(""),
    BOLD("**"),
    ITALIC("*"),
    UNDER_LINE("_"),
    STRIKE_THROUGH("~~"),
    CODE_BLOCK("```"),
    CODE_LINE("`");

    private final String format;

    Format(final String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format;
    }

    public String apply(@Nullable final CharSequence string) {
        return this.format + string + this.format;
    }

    @Nullable
    public static Format byName(final String name) {
        for (Format value : values()) {
            if (value.name().toLowerCase(Locale.ROOT).equalsIgnoreCase(name)) {
                return value;
            }
        }

        return null;
    }
}