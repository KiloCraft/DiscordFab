package com.github.hansi132.discordfab.discordbot.command.argument;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.IDiscordCommandSource;
import com.github.hansi132.discordfab.discordbot.util.FabUtil;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ActivityTypeArgument implements ArgumentType<Activity.ActivityType> {
    private static final List<String> EXAMPLES = Lists.newArrayList("playing", "listening");
    private static final DynamicCommandExceptionType INVALID_ACTIVITY_EXCEPTION = new DynamicCommandExceptionType((object) ->
            () -> "**" + object + "** is not a valid Activity Type"
    );

    public static ActivityTypeArgument activity() {
        return new ActivityTypeArgument();
    }

    public static Activity.ActivityType getActivity(CommandContext<BotCommandSource> context, String name) {
        return context.getArgument(name, Activity.ActivityType.class);
    }

    @Override
    public Activity.ActivityType parse(StringReader reader) throws CommandSyntaxException {
        final String string = reader.readUnquotedString();
        Activity.ActivityType activityType = FabUtil.activityTypeFromString(string);
        if (activityType == null) {
            throw INVALID_ACTIVITY_EXCEPTION.create(string);
        }

        return activityType;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> strings = Lists.newArrayList();
        for (Activity.ActivityType value : Activity.ActivityType.values()) {
            if (value == Activity.ActivityType.CUSTOM_STATUS) {
                continue;
            }

            strings.add(value.name().toLowerCase());
        }

        return IDiscordCommandSource.suggestMatching(strings, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
