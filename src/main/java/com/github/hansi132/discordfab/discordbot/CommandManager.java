package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.api.command.exception.BotCommandException;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.github.hansi132.discordfab.discordbot.commands.PingCommand;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Locale;
import java.util.Map;

public class CommandManager {
    private static final Logger LOGGER = LogManager.getLogger();
    private static boolean isDevelopment;
    private final Map<String, DiscordFabCommand> commands;
    private final CommandDispatcher<BotCommandSource> dispatcher;

    public CommandManager(@NotNull final DiscordFab discordFab) {
        isDevelopment = discordFab.isDevelopment();
        this.commands = Maps.newHashMap();
        this.dispatcher = new CommandDispatcher<>();

        this.register(new PingCommand());
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

    public int execute(@NotNull final BotCommandSource executor, @NotNull final String input) {
        final StringReader reader = new StringReader(input);
        if (reader.canRead() && reader.getString().startsWith("k!")) {
            reader.setCursor(2);
        }

        byte index = 0;
        try {
            try {
                index = (byte) this.dispatcher.execute(reader, executor);
            } catch (BotCommandException e) {
                executor.sendError(e.getMessage()).queue();
                return index;
            } catch (CommandSyntaxException e) {
                executor.sendError(e.getMessage()).queue();
            }
        } catch (Exception e) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("An unexpected error occurred while trying to execute that command")
                    .setDescription(e.getMessage() == null ? e.getClass().getName() : e.getMessage());

            if (isDevelopment) {
                builder.appendDescription("\n" + Messages.exceptionToMessage(e, 6));
                LOGGER.error("Command exception {}", input, e);
                executor.sendError(Messages.getInnermostMessage(e)).queue();
            } else {
                LOGGER.error("'{}' threw an exception", input, e);
            }

            executor.sendError(builder).queue();
            return -1;
        }

        return index;
    }
}
