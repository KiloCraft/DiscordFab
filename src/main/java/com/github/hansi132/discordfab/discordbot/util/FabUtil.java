package com.github.hansi132.discordfab.discordbot.util;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.UUID;

public class FabUtil {
    public static UUID uuidFromShortenedUuidString(@NotNull final String id) {
        return new UUID(
                new BigInteger(id.substring(0, 16), 16).longValue(),
                new BigInteger(id.substring(16), 16).longValue()
        );
    }

    @Nullable
    public static Activity.ActivityType activityTypeFromString(@NotNull final String string) {
        Activity.ActivityType type = null;
        for (Activity.ActivityType value : Activity.ActivityType.values()) {
            if (value.name().equalsIgnoreCase(string)) {
                type = value;
                break;
            }
        }

        return type == Activity.ActivityType.CUSTOM_STATUS ? null : type;
    }

    @Nullable
    public static OnlineStatus onlineStatusFromString(@NotNull final String string) {
        OnlineStatus status = null;
        for (OnlineStatus value : OnlineStatus.values()) {
            if (value.name().equalsIgnoreCase(string)) {
                status = value;
                break;
            }
        }

        return status == OnlineStatus.UNKNOWN ? null :status;
    }
}
