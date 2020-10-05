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

    @Setting(value = "prefix", comment = "Variables: %name%")
    public String prefix;

    @Setting(value = "sensitiveWordFormat", comment = "The format the sensitive words will be in, Variables: %word%")
    public String sensitiveWordFormat = "**%word%**";

    public ChatChannelSynchronizerConfigSection() {
        this("&8[&aChannel&8] &7@&f%name%");
    }

    public ChatChannelSynchronizerConfigSection(final String defaultPrefix) {
        this.prefix = defaultPrefix;
    }

}
