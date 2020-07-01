package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.user.DiscordBroadcaster;
import com.github.hansi132.discordfab.discordbot.util.DatabaseUtils;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.user.User;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class GameChatSynchronizer {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ShardManager SHARD_MANAGER = DiscordFab.getBot();
    private final Map<UUID, net.dv8tion.jda.api.entities.User> map = Maps.newHashMap();
    private final DiscordBroadcaster discordBroadcaster;
    private final Guild guild = DISCORD_FAB.getGuild();

    public GameChatSynchronizer() {
        this.discordBroadcaster = new DiscordBroadcaster();
    }

    public void onGameChat(@NotNull final User user, @NotNull final String string) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();

        if (DatabaseUtils.isLinked(user.getUuid())) {
            net.dv8tion.jda.api.entities.User discordUser = this.getJDAUser(user.getUuid());
            if (discordUser.getAvatarUrl() != null) {
                builder.setAvatarUrl(discordUser.getAvatarUrl());
            }
        } else {

        }

        this.discordBroadcaster.send(builder.build());
    }

    private net.dv8tion.jda.api.entities.User getJDAUser(@NotNull final UUID uuid) {
        if (this.map.containsKey(uuid)) {
            return this.map.get(uuid);
        }

        long discordId = DatabaseUtils.getLinkedUserId(uuid);
        net.dv8tion.jda.api.entities.User user = Objects.requireNonNull(guild.getMemberById(discordId)).getUser();
        this.map.put(uuid, user);
        return user;
    }

    public void onDiscordChat() {

    }

    private static String getMCAvatarURL(@NotNull final UUID uuid) {
        return "https://crafatar.com/avatars/" + uuid.toString() + "?size=256&overlay";
    }
}
