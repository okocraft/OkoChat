package net.okocraft.okochat.api.event.chat.channelchat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.event.chat.PostChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

@SuppressWarnings("ClassCanBeRecord")
public class PostChannelChatEvent implements ChannelChatEvent, PostChatEvent {

    private final Channel channel;
    private final Sender sender;
    private final String originalMessage;
    private final Component formattedMessage;
    private final Collection<Sender> recipients;

    public PostChannelChatEvent(@NotNull Channel channel, @NotNull Sender sender,
                                @NotNull String originalMessage, @NotNull Component formattedMessage,
                                @NotNull @Unmodifiable Collection<Sender> recipients) {
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
    public @NotNull Sender getSender() {
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
    public @NotNull @Unmodifiable Collection<Sender> getRecipients() {
        return this.recipients;
    }
}
