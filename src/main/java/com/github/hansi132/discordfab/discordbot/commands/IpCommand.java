package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.DiscordBroadcaster;
import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.mojang.brigadier.context.CommandContext;
import org.kilocraft.essentials.api.KiloServer;

public class IpCommand extends DiscordFabCommand {
    public IpCommand() {
        super("ip", "server");
        this.argBuilder.executes(this::execute);
    }

    private int execute(CommandContext<BotCommandSource> ctx) {
        BotCommandSource src = ctx.getSource();
        KiloServer.getServer().registerEvent(new DiscordBroadcaster());
        src.sendFeedback("Ip: 50kilo.org \n")
                .appendFormat("Version: %s",
                        KiloServer.getServer().getMinecraftServer().getVersion()).queue();
        return SUCCESS;
    }
}
