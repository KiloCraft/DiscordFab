package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class EventMessagesConfigSection {

    @Setting
    public String serverStart = "Server Started.";

    @Setting
    public String serverStop = "Server Stopped.";

}
