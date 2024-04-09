package net.okocraft.okochat.core.chat.context;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.PrivateChatContext;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;

public final class PrivateChatContexts {

    public record Preparing(@NotNull Sender sender, @NotNull String message,
                            @NotNull Sender target) implements PrivateChatContext {
    }

    public record Ready(@NotNull Sender sender, @NotNull String message, @NotNull Sender target,
                        @NotNull Component formattedMessage) implements PrivateChatContext {
    }

    private PrivateChatContexts() {
        throw new UnsupportedOperationException();
    }


}
