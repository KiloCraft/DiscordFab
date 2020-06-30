package com.github.hansi132.discordfab.discordbot.api.command.exception;

import com.github.hansi132.discordfab.discordbot.api.text.BrigadierCompatibleDataMessage;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.dv8tion.jda.api.MessageBuilder;

public class DiscordFormattedBuiltInExceptions implements BuiltInExceptionProvider {
    @Override
    public Dynamic2CommandExceptionType doubleTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Double must not be less than **" + min + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType doubleTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Double must not be more than **" + max + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType floatTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Float must not be less than **" + min + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType floatTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Float must not be more than **" + max + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType integerTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Integer must not be less than **" + min + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType integerTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Integer must not be more than **" + max + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType longTooLow() {
        return new Dynamic2CommandExceptionType((found, min) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Long must not be less than **" + min + "**, found " + found).build())
        );
    }

    @Override
    public Dynamic2CommandExceptionType longTooHigh() {
        return new Dynamic2CommandExceptionType((found, max) ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Long must not be more than **" + max + "**, found " + found).build())
        );
    }

    @Override
    public DynamicCommandExceptionType literalIncorrect() {
        return new DynamicCommandExceptionType(expected ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected literal **" + expected + "**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedStartOfQuote() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected quote to start a string").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedEndOfQuote() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Unclosed quoted string").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidEscape() {
        return new DynamicCommandExceptionType(character ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid escape sequence **'" + character + "'** in quoted string").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidBool() {
        return new DynamicCommandExceptionType(value ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid bool, expected true or false but found **'" + value + "'**").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidInt() {
        return new DynamicCommandExceptionType(value ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid integer **'" + value + "'**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedInt() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected integer").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidLong() {
        return new DynamicCommandExceptionType(value ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid long **'" + value + "'**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedLong() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected long").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidDouble() {
        return new DynamicCommandExceptionType(value ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid double **'" + value + "'**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedDouble() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected double").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerInvalidFloat() {
        return new DynamicCommandExceptionType(value ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid float **'" + value + "'**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedFloat() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected float").build())
        );
    }

    @Override
    public SimpleCommandExceptionType readerExpectedBool() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected boolean, true or false").build())
        );
    }

    @Override
    public DynamicCommandExceptionType readerExpectedSymbol() {
        return new DynamicCommandExceptionType(symbol ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Invalid **'" + symbol + "'**").build())
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownCommand() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Unknown or incomplete command!").build())
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherUnknownArgument() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Incorrect argument command!").build())
        );
    }

    @Override
    public SimpleCommandExceptionType dispatcherExpectedArgumentSeparator() {
        return new SimpleCommandExceptionType(
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Expected whitespace to end one argument, but found trailing data").build())
        );
    }

    @Override
    public DynamicCommandExceptionType dispatcherParseException() {
        return new DynamicCommandExceptionType(message ->
                ((BrigadierCompatibleDataMessage) new MessageBuilder("Could not parse command: " + message).build())
        );
    }
}
