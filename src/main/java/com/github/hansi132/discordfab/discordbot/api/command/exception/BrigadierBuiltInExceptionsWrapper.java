package com.github.hansi132.discordfab.discordbot.api.command.exception;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.jetbrains.annotations.NotNull;

public class BrigadierBuiltInExceptionsWrapper {
    private final BuiltInExceptionProvider provider;
    private final BuiltInExceptionProvider defaults;

    public BrigadierBuiltInExceptionsWrapper(@NotNull final BuiltInExceptionProvider provider,
                                             @NotNull final BuiltInExceptionProvider minecraftProvider) {
        this.provider = provider;
        this.defaults = minecraftProvider;
    }

    public <S> void wrap(@NotNull final CommandSyntaxException e, ParseResults<S> results) throws CommandSyntaxException {
        if (e.getType() == defaults.dispatcherUnknownCommand()) {
            throw this.provider.dispatcherUnknownCommand().createWithContext(results.getReader());
        } else if (e.getType() == defaults.dispatcherUnknownArgument()) {
            throw this.provider.dispatcherUnknownArgument().createWithContext(results.getReader());
        } else if (e.getType() == defaults.doubleTooLow()) {
            throw this.provider.doubleTooLow().createWithContext(results.getReader(), 1, 1);
        }

    }
}
