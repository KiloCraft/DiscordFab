package com.github.hansi132.discordfab.discordbot.command.argument.discord;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberArgumentType implements ArgumentType<Member> {
    private static final Pattern USER_ID = Pattern.compile("\\d{17,21}");
    private static final Pattern FULL_TAG = Pattern.compile("(.{2,32})#(\\d{4})");
    private static final Pattern MENTION = Pattern.compile("@.+");
    private static final List<String> EXAMPLES = Lists.newArrayList("Wumpus", "123456789101112131");
    private static final SimpleCommandExceptionType INVALID_MEMBER = new SimpleCommandExceptionType(() -> "Member was not found!");

    public static MemberArgumentType member() {
        return new MemberArgumentType();
    }

    public static Member getMember(CommandContext<BotCommandSource> context, String name) {
        return context.getArgument(name, Member.class);
    }

    @Override
    public Member parse(StringReader reader) throws CommandSyntaxException {
        final String string = reader.readString();
        final Guild guild = DiscordFab.getInstance().getGuild();
        Member member = null;

        System.out.println(string);

        final Matcher idMatcher = USER_ID.matcher(string);
        if (idMatcher.matches()) {
            final long id = Long.parseLong(string);
            member = guild.getMemberById(id);
        }

        final Matcher fullTagMatcher = FULL_TAG.matcher(string);
        if (fullTagMatcher.matches()) {
            member = guild.getMemberByTag(string);
        }

        final Matcher mentionMatcher = MENTION.matcher(string);
        if (mentionMatcher.matches()) {
            List<Member> members = guild.getMembersByEffectiveName(string.substring(1), true);
            if (members.size() > 0) member = members.get(0);
        }

        if (member == null) {
            List<Member> membersByEffectiveName = guild.getMembersByEffectiveName(string, true);
            if (membersByEffectiveName.size() > 0) {
                member = membersByEffectiveName.get(0);
            } else {
                List<Member> membersByName = guild.getMembersByName(string, true);
                if (membersByName.size() > 0) {
                    member = membersByName.get(0);
                } else {
                    throw INVALID_MEMBER.create();
                }
            }
        }

        return member;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
