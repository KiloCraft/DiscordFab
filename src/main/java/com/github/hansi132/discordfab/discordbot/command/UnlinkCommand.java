package com.github.hansi132.discordfab.discordbot.command;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.util.user.LinkedUser;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UnlinkCommand extends DiscordFabCommand {

    public UnlinkCommand(@NotNull CommandCategory category, @NotNull String label) {
        super(category, label);
        this.withDescription("Unlinks your account.");
        this.argBuilder.executes(this::execute);
    }

    private int execute(final CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        MessageChannel channel = src.getChannel();
        Optional<LinkedUser> optional = DiscordFab.getInstance().getUserCache().getByDiscordID(src.getUser().getIdLong());
        if (optional.isPresent()) {
            DiscordFab.getInstance().getUserCache().removeUser(optional.get());
            channel.sendMessage(DISCORD_FAB.getConfig().messages.successfully_unlinked.replace("%player%", optional.get().getMcName())).queue();
            return 1;
        } else {
            channel.sendMessage(DISCORD_FAB.getConfig().messages.notLinked).queue();
            return 0;
        }
    }
}
