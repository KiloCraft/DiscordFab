package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.api.text.DiscordCompatibleTextFormat;
import com.github.hansi132.discordfab.discordbot.config.section.chatsync.ChatSynchronizerConfigSection;
import com.github.hansi132.discordfab.discordbot.user.DiscordBroadcaster;
import com.github.hansi132.discordfab.discordbot.util.DatabaseUtils;
import com.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.entities.Member;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.api.user.User;
import org.kilocraft.essentials.chat.ServerChat;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class ChatSynchronizer {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ChatSynchronizerConfigSection CONFIG = DISCORD_FAB.getConfig().chatSynchronizer;
    private final Map<UUID, net.dv8tion.jda.api.entities.User> map = Maps.newHashMap();
    private DiscordBroadcaster discordBroadcaster;

    public ChatSynchronizer() {
        //this.discordBroadcaster = new DiscordBroadcaster();
    }

    public void onGameChat(@NotNull final User user, @NotNull final String string) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);

        String content = TextFormat.clearColorCodes(string.replaceAll("@", ""));
        builder.setContent(content);

        this.discordBroadcaster.send(builder.build());
    }

    public void onDiscordChat(final Member member, @NotNull final String string) {
        ServerChat.Channel.PUBLIC.send(
                new LiteralText("")
                        .append(
                                new LiteralText(ServerChat.Channel.PUBLIC.getPrefix()
                                        .replace("%USER_RANKED_DISPLAYNAME%", member.getEffectiveName())
                                ).styled((style) ->
                                        style.setHoverEvent(
                                                new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                        new LiteralText("From Discord").formatted(Formatting.BLUE))
                                        )
                                ))
                        .append(" ")
                        .append(DiscordCompatibleTextFormat.clearAllDiscord(string))
        );
    }

    @Nullable
    private net.dv8tion.jda.api.entities.User getJDAUser(@NotNull final UUID uuid) {
        if (this.map.containsKey(Objects.requireNonNull(uuid))) {
            return this.map.get(uuid);
        }

        long discordId = DatabaseUtils.getLinkedUserId(uuid);
        if (discordId == 0L) {
            return null;
        }

        net.dv8tion.jda.api.entities.User user = DiscordFab.getBot().getUserById(discordId);

        if (user == null) {
            return null;
        }

        this.map.put(uuid, user);
        return user;
    }

    private static String getMCAvatarURL(@NotNull final UUID uuid) {
        MinecraftAvatar.@Nullable RenderType renderType = MinecraftAvatar.RenderType.getByName(CONFIG.renderOptions.renderType);
        if (renderType == null) {
            renderType = MinecraftAvatar.RenderType.AVATAR;
        }

        return MinecraftAvatar.generateUrl(uuid, renderType, CONFIG.renderOptions.size, CONFIG.renderOptions.showOverlay);
    }

    public void onUserJoin(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(user.getDisplayName() + " Joined the game.");

        this.discordBroadcaster.send(builder.build());
    }

    public void onUserLeave(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(user.getDisplayName() + " Left the game.");

        this.discordBroadcaster.send(builder.build());
        this.map.remove(user.getId());
    }

    public void setMetaFor(@NotNull final User user, @NotNull final WebhookMessageBuilder builder) {
        if (DatabaseUtils.isLinked(user.getUuid())) {
            net.dv8tion.jda.api.entities.User discordUser = this.getJDAUser(user.getUuid());
            if (discordUser != null && discordUser.getAvatarUrl() != null) {
                builder.setAvatarUrl(discordUser.getAvatarUrl());
                builder.setUsername(discordUser.getName());
            }
        } else {
            builder.setAvatarUrl(getMCAvatarURL(user.getUuid()));
            builder.setUsername(user.getUsername());
        }
    }

    public void shutdown() {
        this.discordBroadcaster.getClient().close();
    }
}
