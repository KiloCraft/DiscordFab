package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatChannelSynchronizerConfigSection {

    @Setting
    public boolean enabled = true;

    @Setting
    public String webhookUrl = "";

    @Setting
    public long discordChannelId = 0L;

}
