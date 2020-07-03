package com.github.hansi132.discordfab.discordbot.util;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.config.section.DefaultEmbedConfigSection;

import java.awt.*;

public class EmbedUtil {
    private static final DiscordFab DISCORD_FAB = DiscordFab.getInstance();
    private static final DefaultEmbedConfigSection CONFIG = DISCORD_FAB.getConfig().defaultEmbed;
    private final Color DEFAULT_COLOR;
    private final String AUTHOR_LINK, AUTHOR_ICON_URL;

    public EmbedUtil() {
        DEFAULT_COLOR = Color.decode(CONFIG.color);
        AUTHOR_LINK = CONFIG.authorLink;
        AUTHOR_ICON_URL = CONFIG.authorIconURL;
    }

    public Color getDefaultColor() {
        return DEFAULT_COLOR;
    }

    public String getAuthorLink() {
        return AUTHOR_LINK;
    }

    public String getAuthorIconURL() {
        return AUTHOR_ICON_URL;
    }
}
