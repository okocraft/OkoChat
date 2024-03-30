package net.okocraft.okochat.core.event.chat.privatechat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.core.event.chat.PostChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class PostPrivateChatEvent implements PrivateChatEvent, PostChatEvent {

    private final ChannelMember sender;
    private final ChannelMember target;
    private final String originalMessage;
    private final Component formattedMessage;

    @ApiStatus.Internal
    public PostPrivateChatEvent(@NotNull ChannelMember sender, @NotNull ChannelMember target, @NotNull String originalMessage, @NotNull Component formattedMessage) {
        this.sender = sender;
        this.target = target;
        this.originalMessage = originalMessage;
        this.formattedMessage = formattedMessage;
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
    public @NotNull Component getFormattedMessage() {
        return this.formattedMessage;
    }

    @Override
    public @NotNull @Unmodifiable Collection<ChannelMember> getRecipients() {
        return List.of(this.sender, this.target);
    }
}
