package com.github.hansi132.discordfab.discordbot;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.section.messagesync.ChatSynchronizerConfigSection;
import com.github.hansi132.discordfab.discordbot.integration.UserSynchronizer;
import com.github.hansi132.discordfab.discordbot.util.MinecraftAvatar;
import com.google.common.collect.Maps;
import net.dv8tion.jda.api.entities.Member;

import net.dv8tion.jda.api.entities.Message;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kilocraft.essentials.api.KiloServer;
import org.kilocraft.essentials.api.text.TextFormat;
import org.kilocraft.essentials.api.user.OnlineUser;
import org.kilocraft.essentials.api.user.User;
import org.kilocraft.essentials.chat.KiloChat;
import org.kilocraft.essentials.chat.ServerChat;
import org.kilocraft.essentials.chat.TextMessage;
import org.kilocraft.essentials.commands.CommandUtils;
import org.kilocraft.essentials.util.RegexLib;
import org.kilocraft.essentials.util.text.Texter;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

public class ChatSynchronizer {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final ChatSynchronizerConfigSection CONFIG = DISCORD_FAB.getConfig().chatSynchronizer;
    private static final Pattern LINK_PATTERN = Pattern.compile(RegexLib.URL.get());
    private static final int LINK_MAX_LENGTH = 20;
    private final WebhookClientHolder webhookClientHolder;
    private final Map<UUID, net.dv8tion.jda.api.entities.User> map = Maps.newHashMap();

    private static final String PUBLIC_CHAT_ID = "game_chat";
    private static final String STAFF_CHAT_ID = "game_chat";
    private static final String BUILDER_CHAT_ID = "game_chat";
    private static final String SOCIAL_SPY_ID = "social_spy";

    public ChatSynchronizer() {
        this.webhookClientHolder = new WebhookClientHolder();
        this.webhookClientHolder.addClient(PUBLIC_CHAT_ID, CONFIG.webhookUrl);
        this.webhookClientHolder.addClient(STAFF_CHAT_ID, CONFIG.staffChatWebhookUrl);
        this.webhookClientHolder.addClient(BUILDER_CHAT_ID, CONFIG.builderChatWebhookUrl);
        this.webhookClientHolder.addClient(SOCIAL_SPY_ID, CONFIG.socialSpyWebhookUrl);
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

    private WebhookClient getClientFor(@NotNull final ServerChat.Channel channel) {
        switch (channel) {
            case PUBLIC:
                return this.webhookClientHolder.getClient(PUBLIC_CHAT_ID);
            case STAFF:
                return this.webhookClientHolder.getClient(STAFF_CHAT_ID);
            case BUILDER:
                return this.webhookClientHolder.getClient(BUILDER_CHAT_ID);
            default:
                return null;
        }
    }

    public void onGameDirectChat(@NotNull final ServerCommandSource source, @NotNull final OnlineUser receiver, @NotNull final String message) {
        final WebhookClient client = this.webhookClientHolder.getClient(SOCIAL_SPY_ID);
        if (client == null) {
            return;
        }

        final WebhookMessageBuilder builder = new WebhookMessageBuilder().setContent(
                TextFormat.clearColorCodes(message.replaceAll("@", ""))
        );

        if (CommandUtils.isPlayer(source)) {
            setMetaFor(KiloServer.getServer().getOnlineUser(source.getName()), builder);
        } else {
            builder.setUsername("Server");
        }

        builder.setContent(
                CONFIG.socialSpyFormat.replace("%message%", message)
                        .replace("%source%", source.getName())
                        .replace("%target%", receiver.getName())
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

    private void sendToGame(final Member member, @NotNull final Text content) {
        MutableText text = new TextMessage(CONFIG.messages.prefix
                .replace("%name%", member.getEffectiveName())).toText()
                .append(content);
        KiloChat.broadCast(text);
    }

    public void onDiscordChat(final Member member, @NotNull final String string, List<Message.Attachment> attachments) {
        for (Message.Attachment attachment : attachments) {
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
            sendToGame(member, text);
        }
        if (!string.equals("")) {
            sendToGame(member, new TextMessage(string).toText());
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
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userJoin.replace("%name%", user.getName()));

        this.webhookClientHolder.send(PUBLIC_CHAT_ID, builder.build());
    }

    public void onUserLeave(@NotNull final User user) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        setMetaFor(user, builder);
        builder.setContent(CONFIG.messages.userLeave.replace("%name%", user.getName()));

        this.webhookClientHolder.send(PUBLIC_CHAT_ID, builder.build());
        this.map.remove(user.getId());
    }

    public void setMetaFor(@NotNull final User user, @NotNull final WebhookMessageBuilder builder) {
        builder.setAvatarUrl(getMCAvatarURL(user.getUuid()));
        builder.setUsername(user.getUsername());
    }

    public void shutdown() {
        this.webhookClientHolder.closeAll();
    }
}
