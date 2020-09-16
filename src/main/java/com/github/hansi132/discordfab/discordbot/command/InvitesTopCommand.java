package com.github.hansi132.discordfab.discordbot.command;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InvitesTopCommand extends DiscordFabCommand {

    public InvitesTopCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        final RequiredArgumentBuilder<BotCommandSource, Integer> page = argument("page", IntegerArgumentType.integer(1));
        page.executes(ctx -> execute(ctx, StringArgumentType.getString(ctx, "type"), IntegerArgumentType.getInteger(ctx, "page")));
        final RequiredArgumentBuilder<BotCommandSource, String> type = argument("type", StringArgumentType.word());
        type.executes(ctx -> execute(ctx, StringArgumentType.getString(ctx, "type"), 1));
        type.then(page);
        this.argBuilder.then(type);
        this.argBuilder.executes(ctx -> execute(ctx, "linked", 1));
    }

    private static Map<Long, Integer> sortByComparator(Map<Long, Integer> unsortedMap) {

        List<Map.Entry<Long, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // Maintaining insertion order with the help of LinkedList
        Map<Long, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    public int execute(CommandContext<BotCommandSource> ctx, String type, int page) {
        HashMap<Long, Integer> invites = new HashMap<>();
        for (long inviterid : DiscordFab.getInstance().getInviteTracker().getInviterIDs()) {
            switch (type) {
                case "total":
                    invites.put(inviterid, DiscordFab.getInstance().getInviteTracker().getTotalInvites(inviterid));
                    break;
                case "valid":
                    invites.put(inviterid, DiscordFab.getInstance().getInviteTracker().getValidInvites(ctx.getSource().getGuild(), inviterid));
                    break;
                case "linked":
                    invites.put(inviterid, DiscordFab.getInstance().getInviteTracker().getLinkedInvites(ctx.getSource().getGuild(), inviterid));
                    break;
                default:
                    ctx.getSource().sendFeedback("Invalid type.").queue();
                    return 0;
            }
        }

        int maxPage = (invites.size() / 10);
        page = MathHelper.clamp(page, 1, maxPage);
        int from = 10 * page; //This is the entry number on which the page should start
        int to = (10 * (page + 1)) - 1;
        AtomicInteger j = new AtomicInteger();
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Top " + type + " invites (" + page + 1 + " / " + maxPage + 1 + ")");
        builder.setColor(DiscordFab.getInstance().getEmbedUtil().getDefaultColor());
        sortByComparator(invites).forEach((inviterid, i) -> {
            if (from <= j.get() && to >= j.get()) {
                builder.addField(j.get() + 1 + ".", "<@" + inviterid + "> " + i, false);
                j.getAndIncrement();
            }
        });
        ctx.getSource().sendFeedback(builder.build()).queue();

        return 1;
    }

}
