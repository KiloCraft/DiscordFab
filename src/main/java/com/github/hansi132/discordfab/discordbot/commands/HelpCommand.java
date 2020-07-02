package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.util.Map;

public class HelpCommand extends DiscordFabCommand {
    public HelpCommand() {
        super("help", "This is the help message");
        this.argBuilder.executes(this::executes);
    }

    private int executes(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        DiscordFab fab = DiscordFab.getInstance();
        Map<String, DiscordFabCommand> commandMap = fab.getCommandManager().getCommands();

        //Here we can remove the commands.
        commandMap.remove("help");

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("List of Commands");
        builder.setColor(Color.GREEN);

        for (Map.Entry<String, DiscordFabCommand> entry : commandMap.entrySet()){
            builder.addField(entry.getKey(), entry.getValue().getDescription(), false);
        }

        src.getChannel().sendMessage(builder.build()).queue();

        return SUCCESS;
    }
}
