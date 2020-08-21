package com.github.hansi132.discordfab.discordbot.config.section.messagesync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSyncMessagesConfigSection {

    @Setting(comment = "Variables: %name%")
    public String userJoin = "%name% has joined the game.";

    @Setting(comment = "Variables: %name%")
    public String userLeave = "%name% has left the game.";

}
