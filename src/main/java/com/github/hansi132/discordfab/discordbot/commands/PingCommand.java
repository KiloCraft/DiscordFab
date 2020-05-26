package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;

public class PingCommand extends DiscordFabCommand {
    public PingCommand() {
        super("ping");
        this.argBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        ctx.getSource().sendFeedback("Hello");
        return SUCCESS;
    }

}
