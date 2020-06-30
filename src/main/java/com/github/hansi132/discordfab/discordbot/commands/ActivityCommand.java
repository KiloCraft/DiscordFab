package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;

import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;

public class ActivityCommand extends DiscordFabCommand {

    public ActivityCommand() {
        super("activity");
        this.withDescription("Set the activity of the bot.");
        ArgumentCommandNode<BotCommandSource, String> type = argument("type", string())
                .build();

        final ArgumentCommandNode<BotCommandSource, String> stringArgument = argument("activity", greedyString())
                .executes(this::execute)
                .build();

        type.addChild(stringArgument);
        cmdNode.addChild(type);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        String activity = StringArgumentType.getString(ctx, "activity");
        String type = StringArgumentType.getString(ctx, "type");
        String outputType = type.substring(0,1).toUpperCase() + type.substring(1);
        String concatType = " ";

        if(type.equals("playing")) {
            type = "default";
        }

        if(type.equals("listening")) {
            concatType = " to ";
        }


        assert src.getMember() != null;
        if (src.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            src.getJDA().getPresence().setActivity(Activity.of(Activity.ActivityType.valueOf(type.toUpperCase()), activity, "50kilo.org"));
            src.getChannel().sendMessage("Activity sat to: " + outputType + concatType + activity).queue();
        } else {
            src.getChannel().sendMessage("You are not admin and can not use this command").queue();
        }

        return SUCCESS;
    }
}
