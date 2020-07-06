package com.github.hansi132.discordfab.discordbot.commands;

import com.github.hansi132.discordfab.discordbot.api.command.BotCommandSource;
import com.github.hansi132.discordfab.discordbot.api.command.CommandCategory;
import com.github.hansi132.discordfab.discordbot.api.command.DiscordFabCommand;
import com.github.hansi132.discordfab.discordbot.commands.argument.AvatarRenderTypeArgument;
import com.github.hansi132.discordfab.discordbot.commands.argument.MinecraftPlayerArgument;
import com.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.util.NameLookup;

import java.awt.*;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class MinecraftAvatarCommand extends DiscordFabCommand {
    private static final int DEFAULT_SIZE = 512;

    public MinecraftAvatarCommand(@NotNull CommandCategory category, @NotNull String label, @Nullable String... alias) {
        super(category, label, alias);
        this.withDescription("Get someone's Minecraft Avatar!");

        RequiredArgumentBuilder<BotCommandSource, GameProfile> player = argument("player", MinecraftPlayerArgument.player())
                .executes((ctx) -> this.execute(ctx, MinecraftAvatar.RenderType.BODY, DEFAULT_SIZE, true));

        RequiredArgumentBuilder<BotCommandSource, MinecraftAvatar.RenderType> renderType = argument("renderType", AvatarRenderTypeArgument.renderType())
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), DEFAULT_SIZE, true));

        RequiredArgumentBuilder<BotCommandSource, Integer> size = argument("size", IntegerArgumentType.integer(16, 512))
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), IntegerArgumentType.getInteger(ctx, "size"), true));

        RequiredArgumentBuilder<BotCommandSource, Boolean> overlay = argument("overlay", BoolArgumentType.bool())
                .executes((ctx) -> this.execute(ctx, AvatarRenderTypeArgument.getRenderType(ctx, "renderType"), IntegerArgumentType.getInteger(ctx, "size"), BoolArgumentType.getBool(ctx, "overlay")));

        size.then(overlay);
        renderType.then(size);
        player.then(renderType);
        this.argBuilder.then(player);
    }


    private int execute(final CommandContext<BotCommandSource> ctx,
                        final MinecraftAvatar.RenderType renderType,
                        int size, boolean overlay) throws CommandSyntaxException {
        final BotCommandSource src = ctx.getSource();
        final String username = MinecraftPlayerArgument.getUsername(ctx, "player");

        CompletableFuture.runAsync(() -> {
            GameProfile profile;

            try {
                final String id = NameLookup.getPlayerUUID(username);

                if (id == null) {
                    src.sendFeedback("Invalid username!").queue();
                    return;
                }

                final UUID uuid = new UUID(
                        new BigInteger(id.substring(0, 16), 16).longValue(),
                        new BigInteger(id.substring(16), 16).longValue()
                );
                profile = new GameProfile(uuid, username);
            } catch (IOException e) {
                src.sendFeedback("Invalid username!").queue();
                return;
            }

            src.sendFeedback(
                    new EmbedBuilder()
                            .setTitle("Skin " + renderType.getName() + ": " + profile.getName())
                            .setAuthor(src.getDisplayName(), MinecraftAvatar.API_URL, src.getUser().getAvatarUrl())
                            .setColor(Color.LIGHT_GRAY)
                            .setTimestamp(Instant.now())
                            .setFooter("Powered by: crafatar.com")
                            .setImage(MinecraftAvatar.generateUrl(profile.getId(), renderType, size, overlay))
                            .build()
            ).queue();
        });

        return AWAIT;
    }

}
