package com.github.hansi132.discordfab.discordbot.commands.commands;


import com.github.hansi132.discordfab.discordbot.commands.CommandContext;
import com.github.hansi132.discordfab.discordbot.commands.ICommand;
import org.kilocraft.essentials.api.KiloServer;

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
