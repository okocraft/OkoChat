package net.okocraft.okochat.api.chat.result;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.chat.ngword.DetectedNGWord;
import net.okocraft.okochat.api.permission.Permission;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public sealed interface ChannelChatResult permits ChannelChatResult.ChannelNotFound, ChannelChatResult.EventCancelled, ChannelChatResult.Muted, ChannelChatResult.NGWord, ChannelChatResult.NoPermission, ChannelChatResult.Success {

    record ChannelNotFound(@NotNull Sender sender, @NotNull String message,
                           @Nullable String channelName) implements ChannelChatResult {
    }

    record EventCancelled(@NotNull Sender sender, @NotNull String message,
                          @NotNull Channel channel) implements ChannelChatResult {
    }

    record NoPermission(@NotNull Sender sender, @NotNull String message, @NotNull Channel channel,
                        @NotNull Permission permission) implements ChannelChatResult {
    }

    record Muted(@NotNull Sender sender, @NotNull String message,
                 @NotNull Channel channel) implements ChannelChatResult {
    }

    record NGWord(@NotNull Sender sender, @NotNull String message,
                  @NotNull Channel channel, @NotNull DetectedNGWord detectedNGWord) implements ChannelChatResult {
    }

    record Success(@NotNull Sender sender, @NotNull String message,
                   @NotNull Channel channel, @NotNull Component formattedMessage,
                   @NotNull Collection<Sender> recipients) implements ChannelChatResult {
    }
}
