package net.okocraft.okochat.core.chat.processor;

import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface ProcessContext permits ProcessContext.ChannelChat, ProcessContext.PrivateChat {

    @NotNull Sender sender();

    @NotNull String message();

    record ChannelChat(@NotNull Sender sender, @NotNull String message,
                       @Nullable Channel channel) implements ProcessContext {
    }

    record PrivateChat(@NotNull Sender sender, @NotNull String message,
                       @NotNull Sender target) implements ProcessContext {
    }
}
