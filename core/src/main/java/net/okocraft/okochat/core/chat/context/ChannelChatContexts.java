package net.okocraft.okochat.core.chat.context;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public final class ChannelChatContexts {

    public record Preparing(@NotNull Sender sender, @NotNull String message,
                            @NotNull Channel channel) implements ChannelChatContext {
    }

    public record Formatted(@NotNull Sender sender, @NotNull String message, @NotNull Channel channel,
                            @NotNull Component formattedMessage) implements ChannelChatContext {
    }

    public record Ready(@NotNull Sender sender, @NotNull String message, @NotNull Channel channel,
                        @NotNull Component formattedMessage,
                        @NotNull Collection<Sender> recipients) implements ChannelChatContext {
    }

    private ChannelChatContexts() {
        throw new UnsupportedOperationException();
    }
}
