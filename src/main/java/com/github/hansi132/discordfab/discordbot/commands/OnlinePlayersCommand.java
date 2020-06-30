package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.minecraft.entity.player.PlayerEntity;
import org.kilocraft.essentials.api.KiloServer;

import java.awt.*;
import java.util.Collection;

public class OnlinePlayersCommand extends DiscordFabCommand {
    public OnlinePlayersCommand() {
        super("online", "Gives you the current online players.", "players");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        Collection<PlayerEntity> playerList = KiloServer.getServer().getPlayerList();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Current online players: " + playerList.size());
        eb.setColor(Color.GREEN);
        src.getChannel().sendMessage(eb.build()).queue();

        return SUCCESS;
    }
}

