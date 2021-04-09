package com.github.hansi132.discordfab.discordbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Objects;

public class SuggestionSender {

    public SuggestionSender(GuildMessageReceivedEvent event) {
        String raw = event.getMessage().getContentRaw();
        Member member = event.getMember();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.green);
        embedBuilder.setAuthor(Objects.requireNonNull(member).getEffectiveName());
        embedBuilder.addField("Suggestion", raw, false);
        event.getMessage().delete().queue();
        event.getChannel().sendMessage(embedBuilder.build()).queue(message -> {
            message.addReaction(":upvote:657228604312256521").queue();
            message.addReaction(":downvote:657228570338394142").queue();
        });
    }
}
