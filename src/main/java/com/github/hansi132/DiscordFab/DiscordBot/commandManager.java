package com.github.hansi132.DiscordFab.DiscordBot;


import com.github.hansi132.DiscordFab.DiscordBot.command.CommandContext;
import com.github.hansi132.DiscordFab.DiscordBot.command.ICommand;
import com.github.hansi132.DiscordFab.DiscordBot.command.commands.IpCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class commandManager {
    private final List<ICommand> Commands = new ArrayList<>();
    public commandManager() {
        addCommand(new IpCommand());
    }

    private void addCommand(ICommand cmd){
        boolean nameFound = this.Commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));
        if (nameFound) {
            throw new IllegalArgumentException("A command with this name is already present");
        }
        Commands.add(cmd);
    }

    public List<ICommand> getCommands(){
        return Commands;
    }

    public ICommand getCommand(String search) {
        String searchLower = search.toLowerCase();
        for (ICommand cmd : this.Commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event) {
        if(event.getJDA().getSelfUser().getAsTag().equals("HansiPlaysBotDev#1196")){

            String[] split = event.getMessage().getContentRaw()
                    .replaceFirst("(?i)" + Pattern.quote("dk!"), "")
                    .split("\\s+");

            String invoke = split[0].toLowerCase();
            ICommand cmd = this.getCommand(invoke);

            if (cmd != null){
                event.getChannel().sendTyping().queue();
                List<String> args = Arrays.asList(split).subList(1, split.length);
                CommandContext ctx = new CommandContext(event, args);
                cmd.handle(ctx);
            }
        } else {
            String[] split = event.getMessage().getContentRaw()
                    .replaceFirst("(?i)" + Pattern.quote("k!"), "")
                    .split("\\s+");

            String invoke = split[0].toLowerCase();
            ICommand cmd = this.getCommand(invoke);

            if (cmd != null){
                event.getChannel().sendTyping().queue();
                List<String> args = Arrays.asList(split).subList(1, split.length);
                CommandContext ctx = new CommandContext(event, args);
                cmd.handle(ctx);
            }
        }
    }
}
