package net.okocraft.okochat.api.chat.ngword;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record DetectedNGWord(@NotNull String message,
                             @NotNull NGWord ngWord,
                             @NotNull String detectedWord,
                             @NotNull Optional<String> maskedMessage) {
}
