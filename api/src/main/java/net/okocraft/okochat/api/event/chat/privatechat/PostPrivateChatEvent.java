package net.okocraft.okochat.api.event.chat.privatechat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.event.chat.PostChatEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class PostPrivateChatEvent implements PrivateChatEvent, PostChatEvent {

    private final Sender sender;
    private final Sender target;
    private final String originalMessage;
    private final Component formattedMessage;

    @ApiStatus.Internal
    public PostPrivateChatEvent(@NotNull Sender sender, @NotNull Sender target, @NotNull String originalMessage, @NotNull Component formattedMessage) {
        this.sender = sender;
        this.target = target;
        this.originalMessage = originalMessage;
        this.formattedMessage = formattedMessage;
    }

    @Override
    public @NotNull Sender getSender() {
        return this.sender;
    }

    @Override
    public @NotNull Sender getTarget() {
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
    public @NotNull @Unmodifiable Collection<Sender> getRecipients() {
        return List.of(this.sender, this.target);
    }
}
