package com.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class MCAvatarRenderOptionsConfigSection {
    @Setting(value = "showOverlay", comment = "If true, the skin overlay will be rendered on top of it")
    public boolean showOverlay = true;

    @Setting(value = "renderType", comment = "Types: \"avatar\", \"head\", \"body\"")
    public String renderType = "avatar";

    @Setting(value = "size", comment = "The size of the Avatar, Default: 256")
    public int size = 256;
}
