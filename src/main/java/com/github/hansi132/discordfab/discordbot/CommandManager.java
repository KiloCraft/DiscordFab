package com.github.hansi132.discordfab.discordbot;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.api.command.exception.BotCommandException;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.github.hansi132.discordfab.discordbot.commands.*;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.Map;

public class CommandManager {
    private static final Logger LOGGER = LogManager.getLogger(DiscordFab.class);
    private final Map<String, DiscordFabCommand> commands;
    private final CommandDispatcher<BotCommandSource> dispatcher;

    public CommandManager() {
        this.commands = Maps.newHashMap();
        this.dispatcher = new CommandDispatcher<>();
//        CommandSyntaxException.BUILT_IN_EXCEPTIONS = new DiscordFormattedBuiltInExceptions();

        this.register(new DebugCommand());
        this.register(new PingCommand());
        this.register(new IpCommand());
        this.register(new ActivityCommand());
        this.register(new HelpCommand());
        this.register(new OnlinePlayersCommand());
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

    public void execute(@NotNull final BotCommandSource executor, @NotNull final String input) {
        final StringReader reader = new StringReader(input);
        if (reader.canRead() && reader.getString().startsWith(DiscordFab.getInstance().getConfig().prefix)) {
            reader.setCursor(2);
        }

        String label = reader.getString().split(" ")[0];
        DiscordFabCommand command = DiscordFabCommand.getByLabel(label);
        if (command != null && !command.getPredicate().test(executor)) {
            executor.sendError(
                    new EmbedBuilder().setDescription(
                            DiscordFab.getInstance().getConfig().messages.command_parse_no_permission
                    )
            );
            return;
        }

        try {
            try {
                this.dispatcher.execute(reader, executor);
            } catch (BotCommandException e) {
                executor.sendError(e.getJDAMessage()).queue();
            } catch (CommandSyntaxException e) {
                executor.sendError(new EmbedBuilder().setDescription(e.getMessage())).queue();
            }
        } catch (Exception e) {
            EmbedBuilder builder = new EmbedBuilder()
                    .setTitle("An unexpected error occurred while trying to execute that command");
            builder.addField("Exception message", Messages.getInnermostMessage(e), false);

            if (DiscordFab.getInstance().isDevelopment()) {
                StringBuilder stringBuilder = new StringBuilder();
                StackTraceElement[] elements = e.getStackTrace();
                for (int i = 0; i < Math.min(elements.length, 3); i++) {
                    final StackTraceElement element = elements[i];
                    stringBuilder.append("\n")
                            .append(element.getMethodName()).append("\n ").append(element.getFileName()).append(":")
                            .append(element.getLineNumber());
                }

                builder.addField("Stack trace", stringBuilder.toString(), false);
                LOGGER.error("Command exception {}", input, e);
            } else {
                LOGGER.error("'{}' threw an exception", input, e);
            }

            executor.sendError(builder).queue();
        }

    }
}
