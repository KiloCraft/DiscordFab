package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSynchronizerConfigSection {

    @Setting(value = "toDiscord", comment = "Enables chat synchronization to Discord")
    public boolean toDiscord = true;

    @Setting(value = "toMinecraft", comment = "Enables chat synchronization to Minecraft")
    public boolean toMinecraft = true;


    @Setting(value = "messages")
    public ChatSyncMessagesConfigSection messages = new ChatSyncMessagesConfigSection();

    @Setting("minecraftAvatarRenderOptions")
    public MCAvatarRenderOptionsConfigSection renderOptions = new MCAvatarRenderOptionsConfigSection();

    @Setting(value = "publicChat", comment = "for public chat")
    public ChatChannelSynchronizerConfigSection publicChat = new ChatChannelSynchronizerConfigSection();

    @Setting(value = "staffChat", comment = "for staff chat")
    public ChatChannelSynchronizerConfigSection staffChat = new ChatChannelSynchronizerConfigSection();

    @Setting(value = "builderChat", comment = "for builder chat")
    public ChatChannelSynchronizerConfigSection builderChat = new ChatChannelSynchronizerConfigSection();

    @Setting(value = "socialSpy", comment = "for public chat")
    public SocialSpySynchronizerConfigSection socialSpy = new SocialSpySynchronizerConfigSection();

    @Setting
    public EventMessagesConfigSection event = new EventMessagesConfigSection();

    public boolean isEnabled() {
        return toDiscord || toMinecraft;
    }
}
