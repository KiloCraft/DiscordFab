package com.github.hansi132.discordfab.discordbot.api;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.section.chatsync.ChatSynchronizerConfigSection;
import com.github.hansi132.discordfab.discordbot.user.DiscordBroadcaster;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;
import org.kilocraft.essentials.api.user.OnlineUser;
import org.kilocraft.essentials.util.RegexLib;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class MessageSynchronizer {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ChatSynchronizerConfigSection CONFIG = DISCORD_FAB.getConfig().chatSynchronizer;
    private static final Pattern LINK_PATTERN = Pattern.compile(RegexLib.URL.get());
    private static final int LINK_MAX_LENGTH = 20;
    private final DiscordBroadcaster discordBroadcaster;
    private final Map<UUID, User> map = Maps.newHashMap();

    public MessageSynchronizer() {
        this.discordBroadcaster = new DiscordBroadcaster();

    }

    public void onGameMessage(@NotNull final OnlineUser src, @NotNull final String value) {
    }

    public void onDiscordMessage(@NotNull final Member member, @NotNull final String value, List<Message.Attachment> attachments) {
    }
}
