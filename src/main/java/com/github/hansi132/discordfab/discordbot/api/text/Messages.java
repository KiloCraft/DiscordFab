package com.github.hansi132.discordfab.discordbot.api.text;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Messages {
    public static class Builder implements com.mojang.brigadier.Message {
        private final StringBuilder builder;

        public Builder() {
            this.builder = new StringBuilder();
        }

        public Builder(@Nullable final String content) {
            this();
            this.builder.append(content);
        }

        public Builder(@NotNull Format format, @Nullable final CharSequence content) {
            this();
            this.builder.append(format.apply(content));
        }

        public Builder append(@Nullable final CharSequence sequence) {
            this.builder.append(sequence);
            return this;
        }

        public Builder append(@Nullable final Object object) {
            this.builder.append(object);
            return this;
        }

        public Builder append(@NotNull Format format, @Nullable final CharSequence string) {
            this.builder.append(format.apply(string));
            return this;
        }

        public Builder append(@NotNull Format format, @Nullable final Object object) {
            this.builder.append(format.apply(String.valueOf(object)));
            return this;
        }

        public Builder append(@NotNull final CharSequence sequence, @NotNull final Format... formats) {
            String s = String.valueOf(sequence);
            for (Format format : formats) {
                s = format.apply(s);
            }
            return this.append(s);
        }

        @Override
        public String getString() {
            return builder.toString();
        }

        public Message toJDAMessage() {
            return new MessageBuilder(this.getString()).build();
        }

    }

    public static Message exceptionToMessage(@NotNull final Exception e, final int maxStackTraceElements) {
        MessageBuilder message = new MessageBuilder(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
        StackTraceElement[] elements = e.getStackTrace();
        for (int i = 0; i < Math.min(elements.length, maxStackTraceElements); i++) {
            final StackTraceElement element = elements[i];
            message.append("\n")
                    .append(element.getMethodName())
                    .append("\n ")
                    .append(element.getFileName())
                    .append(":")
                    .append(String.valueOf(element.getLineNumber()));
        }

        return message.build();
    }

    public static String getInnermostMessage(@NotNull final Throwable throwable) {
        return throwable.getCause() == null ?
                throwable.getMessage() == null ?
                        throwable.toString() : throwable.getMessage() :
                getInnermostMessage(throwable.getCause());
    }

}
