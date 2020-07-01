package com.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;

public class ChatSynchronizerConfigSection {

    @Setting(value = "defaultAvatarURL", comment = "The Avatar URL to use if the User isn't linked, " +
            "leave it empty to use their Minecraft Avatar instead")
    public String defaultAvatarURL = "";

    @Setting("minecraftAvatarRenderOptions")
    public MCAvatarRenderOptionsConfigSection renderOptions = new MCAvatarRenderOptionsConfigSection();
}
