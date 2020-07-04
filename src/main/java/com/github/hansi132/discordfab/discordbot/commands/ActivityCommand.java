package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.commands.argument.ActivityTypeArgument;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Locale;
import java.util.Objects;


public class ActivityCommand extends DiscordFabCommand {

    public ActivityCommand() {
        super("activity", Permission.ADMINISTRATOR);
        this.withDescription("Set the activity of the bot.");
        ArgumentCommandNode<BotCommandSource, Activity.ActivityType> type = argument("type", ActivityTypeArgument.activity())
                .build();

        ArgumentCommandNode<BotCommandSource, String> stringArgument = argument("activity", StringArgumentType.greedyString())
                .executes(this::execute)
                .build();

        type.addChild(stringArgument);
        cmdNode.addChild(type);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();

        if (!Objects.requireNonNull(src.getMember()).hasPermission(Permission.ADMINISTRATOR)) {
            src.sendFeedback("Sorry you do not have the right permission");
        }

        Activity.ActivityType activityType = ActivityTypeArgument.getActivity(ctx, "type");
        String string = StringArgumentType.getString(ctx, "activity");

        src.getJDA().getPresence().setActivity(Activity.of(activityType, string));

        src.sendFeedback("Set Activity to **%s** %s", activityType.toString().toLowerCase(Locale.ROOT), string).queue();
        return SUCCESS;
    }
}
