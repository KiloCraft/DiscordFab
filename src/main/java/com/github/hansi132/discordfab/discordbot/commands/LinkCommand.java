package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;

import java.util.Random;

public class LinkCommand extends DiscordFabCommand {
    public LinkCommand() {
        super("link");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();

        //TODO store this in a db.
        String discordId = src.getEvent().getAuthor().getId();
        String discordName = src.getEvent().getAuthor().getAsTag();

        Random random = new Random();
        int linkKey = random.nextInt(10000);
        //TODO check db if it exist -> create new, and store to db.



        return 1;
    }

}
