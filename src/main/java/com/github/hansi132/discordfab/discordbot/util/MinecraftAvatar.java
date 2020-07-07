package com.github.hansi132.discordfab.discordbot.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class MinecraftAvatar {
    public static final String API_URL = "https://crafatar.com/";

    public static String generateUrl(@NotNull final UUID uuid,
                                     @NotNull final RenderType renderType,
                                     final int size,
                                     final boolean overlay) {
        StringBuilder builder = new StringBuilder(API_URL)
                .append(renderType.code).append('/').append(uuid)
                .append("?size=").append(size);

        if (overlay) {
            builder.append("&overlay");
        }

        return builder.toString();
    }

    public static String generate(@NotNull final UUID uuid,
                                  @NotNull final RenderType renderType) {

        return "";
    }

    public enum RenderType {
        AVATAR("Avatar", "avatars", Modifier.ALL),
        HEAD("Head", "renders/head", Modifier.ALL),
        BODY("Body", "renders/body", Modifier.ALL),
        CAPE("Cape", "cape", Modifier.MODEL),
        SKIN("Skin", "skins", Modifier.MODEL);

        private final String name;
        private final String code;
        private final Modifier modifier;

        RenderType(final String name, final String code, final Modifier modifier) {
            this.name = name;
            this.code = code;
            this.modifier = modifier;
        }

        public String getName() {
            return this.name;
        }

        public Modifier getModifier() {
            return this.modifier;
        }

        @Nullable
        public static MinecraftAvatar.RenderType getByName(@NotNull final String name) {
            for (MinecraftAvatar.RenderType value : values()) {
                if (name.equalsIgnoreCase(value.name)) {
                    return value;
                }
            }

            return null;
        }

        enum Modifier {
            ALL(true, true, true, true),
            MODEL(false, false, false, true);

            private final boolean size;
            private final boolean scale;
            private final boolean overlay;
            private final boolean model;
            Modifier(boolean size, boolean scale, boolean overlay, boolean model) {
                this.size = size;
                this.scale = scale;
                this.overlay = overlay;
                this.model = model;
            }

            public boolean isSize() {
                return size;
            }

            public boolean isScale() {
                return scale;
            }

            public boolean isOverlay() {
                return overlay;
            }

            public boolean isModel() {
                return model;
            }
        }
    }
}
