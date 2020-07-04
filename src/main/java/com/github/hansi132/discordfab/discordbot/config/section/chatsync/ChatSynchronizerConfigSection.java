package com.github.hansi132.discordfab.discordbot.config.section.chatsync;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class ChatSynchronizerConfigSection {

    @Setting(value = "to_discord", comment = "Enables chat synchronization to Discord")
    public boolean to_discord = true;

    @Setting(value = "to_minecraft", comment = "Enables chat synchronization to Minecraft")
    public boolean to_minecraft = true;

    @Setting(value = "chat_channel_id", comment = "The Id for the Channel you want to be the" +
            " synchronized channel between the game and discord")
    public long chat_channel_id = 123456789101112131L;

    @Setting(value = "default_avatar_url", comment = "The Avatar URL to use if the User isn't linked, " +
            "leave it empty to use their Minecraft Avatar instead")
    public String default_avatar_url = "";

    @Setting("minecraft_avatar_render_options")
    public MCAvatarRenderOptionsConfigSection render_options = new MCAvatarRenderOptionsConfigSection();

    public boolean isEnabled() {
        return to_discord || to_minecraft;
    }
}
