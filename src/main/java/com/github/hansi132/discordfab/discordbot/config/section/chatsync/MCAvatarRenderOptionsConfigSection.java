package com.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;

public class MCAvatarRenderOptionsConfigSection {
    @Setting(value = "showOverlay", comment = "If true, the skin overlay will be rendered on top of it")
    public boolean showOverlay = true;

    @Setting(value = "renderType", comment = "Types: \"avatar\", \"head\", \"body\"")
    public String renderType = "avatar";
}
