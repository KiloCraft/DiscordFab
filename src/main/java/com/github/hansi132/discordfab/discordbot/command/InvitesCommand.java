package com.github.hansi132.discordfab.discordbot.command;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.command.argument.discord.MemberArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class InvitesCommand extends DiscordFabCommand {
    public InvitesCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        final RequiredArgumentBuilder<BotCommandSource, Member> member = argument("member", MemberArgumentType.member())
                .executes(this::executeOther);
        this.argBuilder.then(member);
        this.argBuilder.executes(this::executeSelf);
    }

    private int executeOther(CommandContext<BotCommandSource> ctx) {
        Member member = MemberArgumentType.getMember(ctx, "member");
        execute(ctx, member);
        return 1;
    }

    private int executeSelf(CommandContext<BotCommandSource> ctx) {
        execute(ctx, Objects.requireNonNull(ctx.getSource().getMember()));
        return 1;
    }

    private void execute(CommandContext<BotCommandSource> ctx, Member member) {
        Guild guild = ctx.getSource().getGuild();
        EmbedBuilder builder = new EmbedBuilder()
            .setColor(DISCORD_FAB.getEmbedUtil().getDefaultColor())
            .setTitle("Invited Players");
        builder.addField("Total invites: ", String.valueOf(DISCORD_FAB.getInviteTracker().getTotalInvites(member.getIdLong())), false);
        builder.addField("Valid invites: ", String.valueOf(DISCORD_FAB.getInviteTracker().getValidInvites(guild, member.getIdLong())), false);
        builder.addField("Linked invites: ", String.valueOf(DISCORD_FAB.getInviteTracker().getLinkedInvites(guild, member.getIdLong())), false);
        ctx.getSource().sendFeedback(builder.build()).queue();
    }
}