package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.section.messagesync.ChatChannelSynchronizerConfigSection;
import com.github.hansi132.discordfab.discordbot.config.section.messagesync.ChatSynchronizerConfigSection;
import com.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import com.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.kyori.adventure.text.Component;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.api.text.TextMessage;
import org.kilocraft.essentials.api.user.OnlineUser;
import org.kilocraft.essentials.api.user.User;
import org.kilocraft.essentials.chat.ServerChat;
import org.kilocraft.essentials.commands.CommandUtils;
import org.kilocraft.essentials.util.text.Texter;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatSynchronizer {
    private static final Logger LOGGER = DiscordFab.LOGGER;
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ChatSynchronizerConfigSection CONFIG = DISCORD_FAB.getConfig().chatSynchronizer;
    private static final Pattern SIMPLE_LINK_PATTERN = Pattern.compile("([--:\\w?@%&+~#=]*\\.[a-z]{2,4}/{0,2})((?:[?&](?:\\w+)=(?:\\w+))+|[--:\\w?@%&+~#=]+)?");
    private static final int LINK_MAX_LENGTH = 20;
    private static final String SOCIAL_SPY_ID = "social_spy";
    private final WebhookClientHolder webhookClientHolder;
    private final Map<UUID, net.dv8tion.jda.api.entities.User> map = Maps.newHashMap();

    public ChatSynchronizer() {
        this.webhookClientHolder = new WebhookClientHolder();
        this.load();
    }

    private static String getMCAvatarURL(@NotNull final UUID uuid) {
        MinecraftAvatar.@Nullable RenderType renderType = MinecraftAvatar.RenderType.getByName(CONFIG.renderOptions.renderType);
        if (renderType == null) {
            renderType = MinecraftAvatar.RenderType.AVATAR;
        }

        return MinecraftAvatar.generateUrl(uuid,
                renderType,
                MinecraftAvatar.RenderType.Model.DEFAULT,
                CONFIG.renderOptions.size,
                CONFIG.renderOptions.scale,
                CONFIG.renderOptions.showOverlay
        );
    }

    public static boolean shouldRespondToCommandIn(final long id) {
        for (MappedChannel value : MappedChannel.values()) {
            if (value.config.discordChannelId == id) {
                return false;
            }
        }

        return true;
    }

    public void load() {
        this.webhookClientHolder.clearAll();
        for (MappedChannel mappedChannel : MappedChannel.values()) {
            this.webhookClientHolder.addClient(mappedChannel.id, mappedChannel.config.webhookUrl);
        }
    }

    @Nullable
    private WebhookClient getClientFor(@NotNull final ServerChat.Channel channel) {
        final MappedChannel mapped = MappedChannel.byServerChannel(channel);
        if (mapped == null) {
            return null;
        }

        return this.webhookClientHolder.getClient(mapped.id);
    }

    public void onSocialSpyWarning(@NotNull final ServerCommandSource source, @NotNull final OnlineUser receiver, @NotNull final String raw, final List<String> marked) {
        final WebhookClient client = this.webhookClientHolder.getClient(MappedChannel.SOCIAL_SPY.id);
        if (client == null) {
            return;
        }

        final WebhookMessageBuilder builder = new WebhookMessageBuilder();
        if (CommandUtils.isPlayer(source)) {
            setMetaFor(KiloServer.getServer().getOnlineUser(source.getName()), builder);
        } else {
            builder.setUsername("Server");
        }

        String string = raw;
        for (String s : marked) {
            Matcher matcher = SIMPLE_LINK_PATTERN.matcher(s);
            if (matcher.matches() && (!s.contains("https://") || !s.contains("http://"))) {
                string.replace(s, "https://" + s);
            }

            string = string.replace(s, CONFIG.socialSpy.sensitiveWordFormat.replace("%word%", s));
        }

        builder.setContent(
                CONFIG.socialSpy.prefix.replace("%source%", source.getName())
                        .replace("%target%", receiver.getName()) + " " + string
        );

        client.send(builder.build());
    }

    public void onGameChat(@NotNull final ServerChat.Channel channel, @NotNull final User user, @NotNull final String message) {
        final WebhookClient client = this.getClientFor(channel);
        if (client == null) {
            return;
        }

        final WebhookMessageBuilder builder = new WebhookMessageBuilder().setContent(
                TextFormat.clearColorCodes(message.replaceAll("@", ""))
        );

        setMetaFor(user, builder);
        client.send(builder.build());
    }

    private void sendToGame(@NotNull final MappedChannel mapped, @NotNull final Member member, @NotNull final Text content) {
        if (mapped.channel == null) {
            return;
        }

        MutableText text = (MutableText) new TextMessage(mapped.config.prefix
            .replace("%name%", member.getEffectiveName())).asText();
        text.append(" ").append(content);

        mapped.channel.send(text);
    }

    public void onDiscordChat(@NotNull final TextChannel channel,
                              @NotNull final Member member,
                              @NotNull final Message message) {
        final MappedChannel mappedChannel = MappedChannel.byChannelId(channel.getIdLong());
        if (mappedChannel == null || !mappedChannel.isEnabled() || !mappedChannel.toMinecraft || mappedChannel.channel == null) {
            return;
        }

        for (Message.Attachment attachment : message.getAttachments()) {
            MutableText text;
            if (attachment.isImage()) {
                text = new LiteralText("[IMAGE]");
            } else if (attachment.isVideo()) {
                text = new LiteralText("[VIDEO]");
            } else {
                text = new LiteralText("[FILE]");
            }
            MutableText hover = new LiteralText("")
                    .append(new LiteralText("Name: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getFileName()).formatted(Formatting.AQUA))
                    .append(new LiteralText("\nResolution: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getWidth() + "x" + attachment.getHeight()).formatted(Formatting.AQUA))
                    .append(new LiteralText("\nSize: ").formatted(Formatting.GRAY))
                    .append(new LiteralText(attachment.getSize() / 1024 + "kb").formatted(Formatting.AQUA));
            text.styled(style -> style.withHoverEvent(Texter.Events.onHover(hover)).withClickEvent(Texter.Events.onClickOpen(attachment.getUrl()))).formatted(Formatting.GREEN);
            sendToGame(mappedChannel, member, text);
        }

        if (!message.getContentRaw().equals("")) {
            sendToGame(mappedChannel, member, new TextMessage(message.getContentRaw()).asText());
        }
    }

    @Nullable
    private net.dv8tion.jda.api.entities.User getJDAUser(@NotNull final UUID uuid) {
        if (this.map.containsKey(Objects.requireNonNull(uuid))) {
            return this.map.get(uuid);
        }

        long discordId = UserSynchronizer.getSyncedUserId(uuid);
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

    public void onUserJoin(@NotNull final User user) {
        try {
            UserSynchronizer.syncRoles(user.getUuid());
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.error("Unexpected error while trying to sync user", e);
        }
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userJoin.replace("%name%", user.getName()));

        this.webhookClientHolder.send(MappedChannel.PUBLIC.id, builder.build());
    }

    public void onUserLeave(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userLeave.replace("%name%", user.getName()));

        this.webhookClientHolder.send(MappedChannel.PUBLIC.id, builder.build());
        this.map.remove(user.getId());
    }

    public void broadcast(@NotNull final String message) {
        final WebhookClient client = this.webhookClientHolder.getClient(MappedChannel.PUBLIC.id);
        if (client == null) {
            return;
        }

        final WebhookMessageBuilder builder = new WebhookMessageBuilder().setContent(TextFormat.clearColorCodes(message));
        setServerUserMeta(builder);
        client.send(builder.build());
    }

    public void broadcast(@NotNull final MessageEmbed message) {
        final TextChannel channel = MappedChannel.PUBLIC.getTextChannel();
        if (channel == null) {
            return;
        }

        channel.sendMessage(message);
    }

    public void setMetaFor(@NotNull final User user, @NotNull final WebhookMessageBuilder builder) {
        builder.setAvatarUrl(getMCAvatarURL(user.getUuid()));
        builder.setUsername(user.getUsername());
    }

    public void setServerUserMeta(@NotNull final WebhookMessageBuilder builder) {
        builder.setUsername(DISCORD_FAB.getConfig().serverUserName);
        builder.setAvatarUrl(DISCORD_FAB.getConfig().serverUserAvatarUrl);
    }

    public void shutdown() {
        this.onServerEvent(ServerEvent.STOP);
        this.webhookClientHolder.closeAll();
    }

    public void onServerEvent(@NotNull final ServerEvent event) {
        String message;
        Color color;
        if (event == ServerEvent.START) {
            message = CONFIG.event.serverStart;
            color = Color.GREEN;
        } else {
            message = CONFIG.event.serverStop;
            color = Color.RED;
        }

        final EmbedBuilder builder = new EmbedBuilder().setTitle(message).setColor(color);
        this.broadcast(builder.build());
    }

    private enum MappedChannel {
        PUBLIC("public_chat", CONFIG.publicChat, ServerChat.Channel.PUBLIC),
        STAFF("staff_chat", CONFIG.staffChat, ServerChat.Channel.STAFF),
        BUILDER("builder_chat", CONFIG.builderChat, ServerChat.Channel.BUILDER),
        SOCIAL_SPY("social_spy", CONFIG.socialSpy, null, false);

        final String id;
        final ChatChannelSynchronizerConfigSection config;
        @Nullable
        final ServerChat.Channel channel;
        final boolean toMinecraft;

        MappedChannel(final String id, final ChatChannelSynchronizerConfigSection config, @Nullable final ServerChat.Channel channel) {
            this(id, config, channel, true);
        }

        MappedChannel(final String id, final ChatChannelSynchronizerConfigSection config, @Nullable final ServerChat.Channel channel, final boolean toMinecraft) {
            this.id = id;
            this.config = config;
            this.channel = channel;
            this.toMinecraft = toMinecraft;
        }

        @Nullable
        public static ChatSynchronizer.MappedChannel byServerChannel(@NotNull final ServerChat.Channel channel) {
            for (MappedChannel value : values()) {
                if (value.channel == null) {
                    continue;
                }

                if (value.channel.equals(channel)) {
                    return value;
                }
            }

            return null;
        }

        @Nullable
        public static ChatSynchronizer.MappedChannel byChannelId(final long id) {
            for (MappedChannel value : values()) {
                if (value.config.discordChannelId == id) {
                    return value;
                }
            }

            return null;
        }

        @Nullable
        public TextChannel getTextChannel() {
            return DISCORD_FAB.getGuild().getTextChannelById(this.config.discordChannelId);
        }

        public boolean isEnabled() {
            return this.config.enabled;
        }
    }

    public enum ServerEvent {
        START,
        STOP;
    }
}
