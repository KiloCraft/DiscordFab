package com.github.hansi132.discordfab.discordbot.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MinecraftAvatar {
    private static final String BASE_URL = "https://crafatar.com/";

    public static String generateUrl(@NotNull final UUID uuid,
                                     @NotNull final RenderType renderType,
                                     final int size,
                                     final boolean overlay) {
        StringBuilder builder = new StringBuilder(BASE_URL)
                .append(renderType.code).append('/').append(uuid)
                .append("?size=").append(size);

        if (overlay) {
            builder.append("&overlay");
        }

        return builder.toString();
    }

    public enum RenderType {
        AVATAR("avatars"),
        HEAD("renders/head"),
        BODY("renders/body");

        private final String code;

        RenderType(final String code) {
            this.code = code;
        }

        @Nullable
        public static MinecraftAvatar.RenderType getByName(@NotNull final String name) {
            for (MinecraftAvatar.RenderType value : values()) {
                if (name.equalsIgnoreCase(value.name())) {
                    return value;
                }
            }

            return null;
        }
    }
}
