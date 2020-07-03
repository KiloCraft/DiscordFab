package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;

public class DebugCommand extends DiscordFabCommand {
    public DebugCommand() {
        super("debug");
        this.withDescription("A debug command");
        CommandNode<BotCommandSource> throwNode = literal("throw")
                .then(
                        argument("message", StringArgumentType.greedyString())
                                .executes(this::executeThrow)
                ).build();

        this.cmdNode.addChild(throwNode);
    }

    private int executeThrow(final CommandContext<BotCommandSource> ctx) {
        String message = StringArgumentType.getString(ctx, "message");
        ctx.getSource().sendFeedback("*Wait for it..*").queue();

        throw new NullPointerException(message);
    }
}
