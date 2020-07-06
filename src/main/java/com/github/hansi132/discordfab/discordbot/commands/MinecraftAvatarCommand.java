package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.commands.argument.AvatarRenderTypeArgument;
import com.github.hansi132.discordfab.discordbot.commands.argument.MinecraftPlayerArgument;
import com.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.concurrent.CompletableFuture;

public class MinecraftAvatarCommand extends DiscordFabCommand {
    private static final int DEFAULT_SIZE = 2048;

    public MinecraftAvatarCommand() {
        super("minecraftavatar", "mcavatar");
        this.withDescription("Get someone's Minecraft Avatar!");

        RequiredArgumentBuilder<BotCommandSource, GameProfile> player = argument("player", MinecraftPlayerArgument.player())
                .executes((ctx) -> this.execute(ctx, MinecraftAvatar.RenderType.AVATAR, DEFAULT_SIZE, true));

        RequiredArgumentBuilder<BotCommandSource, MinecraftAvatar.RenderType> renderType = argument("renderType", AvatarRenderTypeArgument.renderType())
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), DEFAULT_SIZE, true));

        RequiredArgumentBuilder<BotCommandSource, Integer> size = argument("size", IntegerArgumentType.integer(128, 10024))
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), IntegerArgumentType.getInteger(ctx, "size"), true));

        RequiredArgumentBuilder<BotCommandSource, Boolean> overlay = argument("overlay", BoolArgumentType.bool())
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), IntegerArgumentType.getInteger(ctx, "size"), BoolArgumentType.getBool(ctx, "overlay")));

        this.argBuilder.then(player.then(renderType).then(size).then(overlay));
    }

    private int execute(final CommandContext<BotCommandSource> ctx,
                        final MinecraftAvatar.RenderType renderType,
                        int size, boolean overlay) {
        final BotCommandSource src = ctx.getSource();
        final GameProfile profile = MinecraftPlayerArgument.getProfile(ctx, "player");

        if (profile.getId() == null) {
            MessageAction action = src.sendFeedback("*Please wait..*");
            action.queue();

            CompletableFuture.runAsync(() -> {

                action.queue(Message::delete);
            });
            return AWAIT;
        }


        final String url = MinecraftAvatar.generateUrl(profile.getId(), renderType, size, overlay);
        ctx.getSource().sendFeedback(url);
        return SUCCESS;
    }

}
