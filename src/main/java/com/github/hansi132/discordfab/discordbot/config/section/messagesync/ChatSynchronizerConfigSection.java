package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSynchronizerConfigSection {

    @Setting(value = "toDiscord", comment = "Enables chat synchronization to Discord")
    public boolean toDiscord = true;

    @Setting(value = "toMinecraft", comment = "Enables chat synchronization to Minecraft")
    public boolean toMinecraft = true;

    @Setting(value = "chatChannelId", comment = "The Id for the Channel you want to be the" +
            " synchronized channel between the game and discord")
    public long chatChannelId = 123456789101112131L;

    @Setting(value = "webhookUrl", comment = "Leave it empty to disable it")
    public String webhookUrl = "";

    @Setting(value = "staffChatWebhookUrl", comment = "Leave it empty to disable it")
    public String staffChatWebhookUrl = "";

    @Setting(value = "builderChatWebhookUrl", comment = "Leave it empty to disable it")
    public String builderChatWebhookUrl = "";

    @Setting(value = "socialSpyChannelWebhookUrl", comment = "Leave it empty to disable it")
    public String socialSpyWebhookUrl = "";

    @Setting(value = "socialSpyFormat", comment = "Values: %message%, %source%, %target%")
    public String socialSpyFormat = "**to %target% -> ** %message%";

    @Setting(value = "messages")
    public ChatSyncMessagesConfigSection messages = new ChatSyncMessagesConfigSection();

    @Setting("minecraftAvatarRenderOptions")
    public MCAvatarRenderOptionsConfigSection renderOptions = new MCAvatarRenderOptionsConfigSection();


    public boolean isEnabled() {
        return toDiscord || toMinecraft;
    }
}
