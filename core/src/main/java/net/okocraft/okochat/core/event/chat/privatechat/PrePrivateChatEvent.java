package net.okocraft.okochat.core.event.chat.privatechat;

import net.okocraft.okochat.core.event.Cancellable;
import net.okocraft.okochat.core.event.chat.PreChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PrePrivateChatEvent implements PrivateChatEvent, PreChatEvent, Cancellable {

    private final ChannelMember sender;
    private final ChannelMember target;
    private final String originalMessage;

    private @Nullable String modifiedMessage = null;
    private boolean cancelled = false;

    @ApiStatus.Internal
    public PrePrivateChatEvent(@NotNull ChannelMember sender, @NotNull ChannelMember target, @NotNull String originalMessage) {
        this.sender = sender;
        this.target = target;
        this.originalMessage = originalMessage;
    }

    @Override
    public @NotNull ChannelMember getSender() {
        return this.sender;
    }

    @Override
    public @NotNull ChannelMember getTarget() {
        return this.target;
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
