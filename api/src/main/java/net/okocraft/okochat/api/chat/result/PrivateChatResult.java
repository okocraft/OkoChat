package net.okocraft.okochat.api.chat.result;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.ngword.DetectedNGWord;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;

public sealed interface PrivateChatResult permits PrivateChatResult.EventCancelled, PrivateChatResult.Muted, PrivateChatResult.NGWord, PrivateChatResult.Success {

    record EventCancelled(@NotNull Sender sender, @NotNull String message,
                          @NotNull Sender target) implements PrivateChatResult {
    }

    record Muted(@NotNull Sender sender, @NotNull String message,
                 @NotNull Sender target) implements PrivateChatResult {
    }

    record NGWord(@NotNull Sender sender, @NotNull String message, @NotNull Sender target,
                  @NotNull DetectedNGWord detectedNGWord) implements PrivateChatResult {
    }

    record Success(@NotNull Sender sender, @NotNull String message,
                   @NotNull Sender target, @NotNull Component formattedMessage) implements PrivateChatResult {
    }
}
