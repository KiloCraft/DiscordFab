package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.kilocraft.essentials.api.KiloEssentials;
import org.kilocraft.essentials.api.user.OnlineUser;

import java.util.List;


public class OnlinePlayersCommand extends DiscordFabCommand {
    public OnlinePlayersCommand() {
        super("online", "players");
        this.withDescription("Gives you the current online players.");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        List<OnlineUser> users = KiloEssentials.getServer().getUserManager().getOnlineUsersAsList();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Online Players");

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < users.size(); i++) {
            stringBuilder.append(users.get(i).getUsername());

            if (i != users.size()) {
                stringBuilder.append(", ");
            }
        }

        builder.addField(users.size() + " online", stringBuilder.toString(), false);
        src.sendFeedback(builder.build()).queue();

        return SUCCESS;
    }
}

