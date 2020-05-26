package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.api.text.Format;
import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;

public class PingCommand extends DiscordFabCommand {
    public PingCommand() {
        super("ping");
        this.argBuilder.executes(this::execute);

        this.argBuilder.then(
                argument("something", IntegerArgumentType.integer(0, 3))
                        .executes((ctx) -> {
                            ctx.getSource().sendFeedback(
                                    new Messages.Builder("You've entered ")
                                            .append(Format.BOLD, IntegerArgumentType.getInteger(ctx, "something"))
                                            .append("! owo").append('\n')
                                            .append("Format Test", Format.BOLD, Format.ITALIC, Format.UNDER_LINE).toJDAMessage()
                            ).queue();

                            return SUCCESS;
                        })
        );
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
