package net.okocraft.okochat.core.event.chat.channelchat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.core.channel.Channel;
import net.okocraft.okochat.core.event.chat.PostChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public class PostChannelChatEvent implements ChannelChatEvent, PostChatEvent {

    private final Channel channel;
    private final ChannelMember sender;
    private final String originalMessage;
    private final Component formattedMessage;
    private final Collection<ChannelMember> recipients;

    public PostChannelChatEvent(@NotNull Channel channel, @NotNull ChannelMember sender,
                                @NotNull String originalMessage, @NotNull Component formattedMessage,
                                @NotNull @Unmodifiable Collection<ChannelMember> recipients) {
        this.channel = channel;
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.formattedMessage = formattedMessage;
        this.recipients = recipients;
    }

    @Override
    public @NotNull Channel getChannel() {
        return this.channel;
    }

    @Override
    public @NotNull ChannelMember getSender() {
        return this.sender;
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
        return this.recipients;
    }
}
