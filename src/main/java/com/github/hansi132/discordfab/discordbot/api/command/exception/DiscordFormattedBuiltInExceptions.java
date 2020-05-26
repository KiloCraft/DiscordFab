package com.github.hansi132.discordfab.discordbot.api.command.exception;

import com.github.hansi132.discordfab.discordbot.api.text.Format;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

public class DiscordFormattedBuiltInExceptions implements BuiltInExceptionProvider {
    @Override
    public Dynamic2CommandExceptionType doubleTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                new Messages.Builder("Double must not be less than ").append(Format.BOLD, min).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType doubleTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                new Messages.Builder("Double must not be more than ").append(Format.BOLD, max).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType floatTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                new Messages.Builder("Float must not be less than ").append(Format.BOLD, min).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType floatTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                new Messages.Builder("Float must not be more than ").append(Format.BOLD, max).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType integerTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                new Messages.Builder("Integer must not be less than ").append(Format.BOLD, min).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType integerTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                new Messages.Builder("Integer must not be more than ").append(Format.BOLD, max).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType longTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                new Messages.Builder("Long must not be less than ").append(Format.BOLD, min).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public Dynamic2CommandExceptionType longTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                new Messages.Builder("Long must not be more than ").append(Format.BOLD, max).append(", found ").append(Format.BOLD, found)
        );
    }

    @Override
    public DynamicCommandExceptionType literalIncorrect() {
        return new DynamicCommandExceptionType(expected ->
                new Messages.Builder("Expected literal ").append(Format.BOLD, expected)
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedStartOfQuote() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected quote to start a string")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedEndOfQuote() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Unclosed quoted string")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidEscape() {
        return new DynamicCommandExceptionType(character ->
                new Messages.Builder("Invalid escape sequence '").append(Format.BOLD, character).append("' in quoted string")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidBool() {
        return new DynamicCommandExceptionType(value ->
                new Messages.Builder("Invalid bool, expected true or false but found '").append(Format.BOLD, value).append("'")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidInt() {
        return new DynamicCommandExceptionType(value ->
                new Messages.Builder("Invalid integer '").append(Format.BOLD, value).append("'")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedInt() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected integer")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidLong() {
        return new DynamicCommandExceptionType(value ->
                new Messages.Builder("Invalid long '").append(Format.BOLD, value).append("'")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedLong() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected long")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidDouble() {
        return new DynamicCommandExceptionType(value ->
                new Messages.Builder("Invalid double '").append(Format.BOLD, value).append("'")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedDouble() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected double")
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidFloat() {
        return new DynamicCommandExceptionType(value ->
                new Messages.Builder("Invalid float '").append(Format.BOLD, value).append("'")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedFloat() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected float")
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedBool() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected true or false")
        );
    }

    @Override
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return new DynamicCommandExceptionType(symbol ->
                new Messages.Builder("Expected '").append(Format.BOLD, symbol).append("'")
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownCommand() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Unknown or incomplete command!")
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownArgument() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Incorrect argument for command!")
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
        return new SimpleCommandExceptionType(
                new Messages.Builder("Expected whitespace to end one argument, but found trailing data")
        );
    }

    @Override
    public DynamicCommandExceptionType dispatcherParseException() {
        return new DynamicCommandExceptionType(message ->
                new Messages.Builder(Format.BOLD, "Could not parse command:").append(' ').append(message)
        );
    }
}
