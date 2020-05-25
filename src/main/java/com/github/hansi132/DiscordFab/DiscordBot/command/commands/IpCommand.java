package com.github.hansi132.DiscordFab.DiscordBot.command.commands;


import com.github.hansi132.DiscordFab.DiscordBot.command.CommandContext;
import com.github.hansi132.DiscordFab.DiscordBot.command.ICommand;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.chat.ServerChat;
import org.kilocraft.essentials.user.setting.Settings;

public class IpCommand implements ICommand {


    @Override
    public void handle(CommandContext ctx) {
        ctx.getChannel().sendMessage("Version :" + KiloServer.getServer().getMinecraftServer().getVersion() + "\nIP : 50kilo.org").queue();
    }

    @Override
    public String getName() {
        return "ip";
    }

    @Override
    public String getHelp() {
        return "Shows the ip of the server and the version";
    }
}
