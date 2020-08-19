package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class SocialSpySynchronizerConfigSection extends ChatChannelSynchronizerConfigSection {
    @Setting
    public String format = "**to %target% ->** %message%";
}
