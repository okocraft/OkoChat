package net.okocraft.okochat.api.event.chat.channelchat;

import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ChannelFindEvent implements ChannelChatEvent, Cancellable {

    private final Channel channel;
    private final Sender sender;
    private final String originalMessage;
    private final String chatMessage;

    private @Nullable Channel modifiedChannel;
    private @Nullable String modifiedChatMessage;
    private boolean cancelled;

    public ChannelFindEvent(@NotNull Channel channel, @NotNull Sender sender,
                            @NotNull String originalMessage, @NotNull String chatMessage) {
        this.channel = channel;
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.chatMessage = chatMessage;
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

    public @NotNull String getChatMessage() {
        return this.chatMessage;
    }

    public void setModifiedChannel(@Nullable Channel modifiedChannel) {
        this.modifiedChannel = modifiedChannel;
    }

    public @NotNull Channel getResultChannel() {
        return Objects.requireNonNullElse(this.modifiedChannel, this.channel);
    }

    public void setModifiedChatMessage(@Nullable String modifiedChatMessage) {
        this.modifiedChatMessage = modifiedChatMessage;
    }

    public @NotNull String getResultChatMessage() {
        return Objects.requireNonNullElse(this.modifiedChatMessage, this.chatMessage);
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
