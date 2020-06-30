package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.api.text.Format;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.mojang.brigadier.context.CommandContext;

public class PingCommand extends DiscordFabCommand {
    public PingCommand() {
        super("ping");
        this.withDescription("Gives you the ping between the bot and discord servers.");
        this.argBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        double ping = src.getJDA().getGatewayPing();
        double restPing = src.getJDA().getRestPing().complete();

        src.sendFeedback(new Messages.Builder(Format.BOLD, "Pong!")).queue(msg ->
                new Messages.Builder(Format.BOLD, "Pong!")
                        .append("The Gateway latency is ").append(Format.BOLD, ping + "ms").append(" and the REST API latency is")
                        .append(Format.BOLD, restPing + "ms").toJDAMessage()
        );

        return (int) ping;

    }

}
