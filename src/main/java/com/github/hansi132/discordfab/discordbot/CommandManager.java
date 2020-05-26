package com.github.hansi132.discordfab.discordbot;


import com.github.hansi132.discordfab.discordbot.commands.CommandContext;
import com.github.hansi132.discordfab.discordbot.commands.ICommand;
import com.github.hansi132.discordfab.discordbot.commands.commands.IpCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {
    private final List<ICommand> Commands = new ArrayList<>();

    public CommandManager() {
        addCommand(new IpCommand());
    }

    private void addCommand(ICommand cmd) {
        boolean nameFound = this.Commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }
        Commands.add(cmd);
    }

    public List<ICommand> getCommands() {
        return Commands;
    }

    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.Commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)) {
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        //Check if the user is the dev bot
        if (event.getJDA().getSelfUser().getAsTag().equals("HansiPlaysBotDev#1196")) {
            //Remove the prefix, split on space.
            String[] command = event.getMessage().getContentRaw()
                    .replaceFirst("(?i)" + Pattern.quote("dk!"), "")
                    .split("\\s+");

            //Setting cmd to
            ICommand cmd = this.getCommand(command[0]);

            if (cmd != null) {
                event.getChannel().sendTyping().queue();
                List<String> args = Arrays.asList(command).subList(1, command.length);
                CommandContext ctx = new CommandContext(event, args);
                cmd.handle(ctx);
            }

        } //Else use the normal prefix
        else {
            String[] command = event.getMessage().getContentRaw()
                    .replaceFirst("(?i)" + Pattern.quote("k!"), "")
                    .split("\\s+");

            ICommand cmd = this.getCommand(command[0]);

            if (cmd != null) {
                event.getChannel().sendTyping().queue();
                List<String> args = Arrays.asList(command).subList(1, command.length);
                CommandContext ctx = new CommandContext(event, args);
                cmd.handle(ctx);
            }
        }
    }
}
