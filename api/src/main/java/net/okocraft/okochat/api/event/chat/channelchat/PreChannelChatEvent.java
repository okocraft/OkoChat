package net.okocraft.okochat.api.event.chat.channelchat;

import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.event.Cancellable;
import net.okocraft.okochat.api.event.chat.PreChatEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PreChannelChatEvent implements ChannelChatEvent, PreChatEvent, Cancellable {

    private final Channel channel;
    private final Sender sender;
    private final String originalMessage;

    private @Nullable String modifiedMessage = null;
    private boolean cancelled = false;

    @ApiStatus.Internal
    public PreChannelChatEvent(@NotNull Channel channel, @NotNull Sender sender, @NotNull String originalMessage) {
        this.channel = channel;
        this.sender = sender;
        this.originalMessage = originalMessage;
    }

    @Override
    public @NotNull Channel getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull Sender getSender() {
        return this.sender;
    }

    @Override
    public @NotNull String getOriginalMessage() {
        return this.originalMessage;
    }

    @Override
    public @NotNull String getResultMessage() {
        return Objects.requireNonNull(this.modifiedMessage, this.originalMessage);
    }

    @Override
    public void setModifiedMessage(@Nullable String modifiedMessage) {
        this.modifiedMessage = modifiedMessage;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
