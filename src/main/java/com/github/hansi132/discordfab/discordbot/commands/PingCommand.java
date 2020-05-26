package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.util.Constants;
import com.mojang.brigadier.context.CommandContext;

public class PingCommand extends DiscordFabCommand {
    public PingCommand() {
        super("ping");
        this.argBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        double ping = src.getJDA().getGatewayPing();
        double restPing = src.getJDA().getRestPing().complete();



        src.sendFeedback("Pong! ")
                .appendFormat("The Gateway latency is %s and the REST API latency is %s",
                        Constants.DECIMAL_FORMAT.format(ping),
                        Constants.DECIMAL_FORMAT.format(restPing)).queue();

        return SUCCESS;
    }

}
