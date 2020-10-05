package com.github.hansi132.discordfab.discordbot.command;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.command.argument.ActivityTypeArgument;
import com.github.hansi132.discordfab.discordbot.command.argument.OnlineStatusArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PresenceCommand extends DiscordFabCommand {

    public PresenceCommand(@NotNull CommandCategory category, @NotNull String label, @NotNull Permission permission) {
        super(category, label, permission);
        this.withDescription("Set the presences of the bot.");

        final LiteralArgumentBuilder<BotCommandSource> status = literal("status");
        final LiteralArgumentBuilder<BotCommandSource> activity = literal("activity");

        final RequiredArgumentBuilder<BotCommandSource, OnlineStatus> onlineStatus = argument("status", OnlineStatusArgument.onlineStatus())
                .executes(this::setStatus);

        status.then(onlineStatus);

        final ArgumentCommandNode<BotCommandSource, Activity.ActivityType> type = argument("type", ActivityTypeArgument.activity())
                .build();

        final ArgumentCommandNode<BotCommandSource, String> stringArgument = argument("activity", StringArgumentType.greedyString())
                .executes(this::setActivity)
                .build();

        type.addChild(stringArgument);
        activity.then(type);

        this.argBuilder.then(status);
        this.argBuilder.then(activity);
    }

    private int setStatus(final CommandContext<BotCommandSource> ctx) {
        final BotCommandSource src = ctx.getSource();
        final OnlineStatus status = OnlineStatusArgument.getStatus(ctx, "status");

        DiscordFab.getBot().setStatus(status);
        src.sendFeedback("Set status to **%s**", status.getKey()).queue();
        return SUCCESS;
    }

    private int setActivity(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();

        Activity.ActivityType activityType = ActivityTypeArgument.getActivity(ctx, "type");
        String string = StringArgumentType.getString(ctx, "activity");

        DiscordFab.getInstance().setActivity(activityType, string);

        src.sendFeedback("Set Activity to **%s** (%s)", string, activityType.toString().toLowerCase(Locale.ROOT)).queue();
        return SUCCESS;
    }
}
