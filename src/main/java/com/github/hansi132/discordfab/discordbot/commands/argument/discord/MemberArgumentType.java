package com.github.hansi132.discordfab.discordbot.commands.argument.discord;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberArgumentType implements ArgumentType<Member> {
    private static final Pattern USER_ID = Pattern.compile("\\d{17,21}");
    private static final Pattern FULL_TAG = Pattern.compile("(.*)(( *#)|#|(# *))(\\d{4})");
    private static final Pattern MENTION = Pattern.compile("<[@#][!&]?[0-9]+>");
    private static final List<String> EXAMPLES = Lists.newArrayList("Wumpus", "123456789101112131");
    private static final SimpleCommandExceptionType INVALID_MEMBER = new SimpleCommandExceptionType(() -> "Member was not found!");

    private MemberArgumentType() {
        this(null);
    }

    @Nullable
    private final BotCommandSource src;

    private MemberArgumentType(@Nullable final BotCommandSource src) {
        this.src = src;
    }

    public static MemberArgumentType member() {
        return new MemberArgumentType();
    }

    public static Member getMember(CommandContext<BotCommandSource> context, String name) throws CommandSyntaxException {
        new MemberArgumentType(context.getSource());
        final Member member = context.getArgument(name, Member.class);

        if (member == null) {
            throw INVALID_MEMBER.create();
        }

        return member;
    }

    @Override
    public Member parse(StringReader reader) {
        final String string = reader.readUnquotedString();
        assert this.src != null;
        final Guild guild = this.src.getGuild();

        System.out.println(string);

        final Matcher idMatcher = USER_ID.matcher(string);
        if (idMatcher.find()) {
            final long id = Long.parseLong(idMatcher.group(1));
            return guild.getMemberById(id);
        }

        final Matcher fullTagMatcher = FULL_TAG.matcher(string);
        if (fullTagMatcher.find()) {
            return guild.getMemberByTag(idMatcher.group(1));
        }

        return null;
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
