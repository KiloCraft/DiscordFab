package com.github.hansi132.discordfab.discordbot.api.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BotCommandSource implements IDiscordCommandSource {
    private final JDA api;
    private final String name;
    private final Guild guild;
    private final TextChannel channel;
    private final User user;
    private final Member member;
    private final GuildMessageReceivedEvent event;

    public BotCommandSource(@NotNull final JDA api,
                            @NotNull final String name,
                            @NotNull final Guild guild,
                            @NotNull final TextChannel channel,
                            @NotNull final User user,
                            @Nullable final Member member,
                            @NotNull final GuildMessageReceivedEvent event) {
        this.api = api;
        this.name = name;
        this.guild = guild;
        this.channel = channel;
        this.user = user;
        this.member = member;
        this.event = event;
    }

    @Override
    public List<Member> getAllGuildMembers() {
        return this.guild.getMembers();
    }

    @Override
    public JDA getJDA() {
        return this.api;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Guild getGuild() {
        return this.guild;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Nullable
    @Override
    public Member getMember() {
        return this.member;
    }

    public boolean isWebhookMessage() {
        return this.event.isWebhookMessage();
    }

    @Override
    public TextChannel getChannel() {
        return this.channel;
    }

    @Override
    public MessageAction sendFeedback(@NotNull CharSequence charSequence) {
        return this.channel.sendMessage(charSequence);
    }

    @Override
    public MessageAction sendFeedback(@NotNull String string, @Nullable Object... objects) {
        return this.channel.sendMessageFormat(string, objects);
    }

    @Override
    public MessageAction sendFeedback(@NotNull Message message) {
        return this.channel.sendMessage(message);
    }

    @Override
    public MessageAction sendFeedback(@NotNull MessageEmbed embed) {
        return this.channel.sendMessage(embed);
    }

    @Override
    public MessageAction sendError(@NotNull CharSequence sequence) {
        return this.sendFeedback(sequence);
    }

    @Override
    public MessageAction sendError(@NotNull EmbedBuilder builder) {
        return this.sendFeedback(builder.setColor(1536).build());
    }

    @Override
    public MessageAction sendError(@NotNull String string, @Nullable Object... objects) {
        return this.sendFeedback(string, objects);
    }

    @Override
    public MessageAction sendError(@NotNull Message message) {
        return this.sendFeedback(message);
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return this.event;
    }

}
