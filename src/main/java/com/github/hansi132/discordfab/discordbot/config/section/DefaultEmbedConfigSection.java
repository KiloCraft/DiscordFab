package com.github.hansi132.discordfab.discordbot.config.section;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class DefaultEmbedConfigSection {

    @Setting(value = "color", comment = "The Default color for the Embed")
    public String color = "#FF8C00";

    @Setting(value = "author_link", comment = "The Default Author Link")
    public String author_link = "https://50kilo.org/";

    @Setting(value = "author_icon_url", comment = "The Default Author ICON URL")
    public String author_icon_url = "";
}
