package com.github.hansi132.discordfab.discordbot.api.command;

import com.github.hansi132.discordfab.discordbot.api.text.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
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

    MessageAction sendFeedback(@NotNull final String string, @Nullable Object... objects);

    MessageAction sendFeedback(@NotNull final Message message);

    MessageAction sendFeedback(@NotNull final MessageEmbed embed);

    MessageAction sendFeedback(@NotNull final Messages.Builder builder);

    MessageAction sendError(@NotNull final CharSequence sequence);

    MessageAction sendError(@NotNull final EmbedBuilder builder);

    MessageAction sendError(@NotNull final Messages.Builder builder);

    MessageAction sendError(@NotNull final String string, @Nullable Object... objects);

    MessageAction sendError(@NotNull final Message message);

    GuildMessageReceivedEvent getEvent();

}
