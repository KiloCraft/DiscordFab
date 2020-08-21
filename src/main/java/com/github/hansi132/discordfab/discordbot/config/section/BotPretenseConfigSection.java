package com.github.hansi132.discordfab.discordbot.config.section;

import com.github.hansi132.discordfab.DiscordFab;
import com.github.hansi132.discordfab.discordbot.util.FabUtil;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.jetbrains.annotations.NotNull;

@ConfigSerializable
public class BotPretenseConfigSection {

    @Setting(value = "type", comment = "Activity Types: Default (Playing), Streaming, Listening")
    public String type = "watching";

    @Setting(value = "value", comment = "The value can be set to \"online\" for showing the online players," +
            "\n otherwise it can be a string or in case of the activity being \"watching\" a URL")
    public String value = "";

    @Setting(value = "status", comment = "The status of the bot user, values: online, dnd, offline, invisible")
    public String status = "online";

    @NotNull
    public Activity.ActivityType getActivityType() {
        Activity.ActivityType type = FabUtil.activityTypeFromString(this.type);
        if (type == null) {
            DiscordFab.LOGGER.error("Invalid activity type found! '{}'", this.type);
            return Activity.ActivityType.DEFAULT;
        }

        return type;
    }

    @NotNull
    public OnlineStatus getOnlineStatus() {
        OnlineStatus status = FabUtil.onlineStatusFromString(this.status);
        if (status == null) {
            DiscordFab.LOGGER.error("Invalid activity type found! '{}'", this.type);
            return OnlineStatus.ONLINE;
        }

        return status;
    }

}
