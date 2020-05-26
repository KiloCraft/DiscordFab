package com.github.hansi132.discordfab.discordbot.api.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IDiscordCommandSource {
    List<Member> getAllGuildMembers();

    JDA getJDA();

    String getName();

    Guild getGuild();

    User getUser();

    @Nullable
    Member getMember();

    TextChannel getChannel();

    MessageAction sendFeedback(@NotNull final CharSequence sequence);

    MessageAction sendFeedback(@NotNull final String string, Object... objects);

    MessageAction sendFeedback(@NotNull final Message message);

    MessageAction sendFeedback(@NotNull final MessageEmbed embed);

    GuildMessageReceivedEvent getEvent();

}
