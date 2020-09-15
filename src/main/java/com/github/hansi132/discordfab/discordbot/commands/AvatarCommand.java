package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.commands.argument.discord.MemberArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AvatarCommand extends DiscordFabCommand {
    public AvatarCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        this.withDescription("Look at other's Avatars");

        RequiredArgumentBuilder<BotCommandSource, Member> member = argument("member", MemberArgumentType.member())
                .executes(this::execute);

        this.argBuilder.then(member);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        final BotCommandSource src = ctx.getSource();
        final Member target = MemberArgumentType.getMember(ctx, "member");

        src.sendFeedback(
                new EmbedBuilder()
                        .setTitle(target.getUser().getAsTag(), target.getUser().getAvatarUrl())
                        .setImage(target.getUser().getAvatarUrl())
                        .setColor(Color.BLACK)
                        .build()
        ).queue();

        return SUCCESS;
    }
}
