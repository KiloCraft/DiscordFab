package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class SpySynchronizerConfigSection {

    @Setting("socialSpyEnabled")
    public boolean socialEnabled = true;

    @Setting("socialSpyChannelWebhookUrl")
    public String socialWebhookUrl = "";

    @Setting("commandSpyEnabled")
    public boolean commandEnabled = true;

    @Setting("commandSpyChannelWebhookUrl")
    public String commandWebhookUrl = "";

}
