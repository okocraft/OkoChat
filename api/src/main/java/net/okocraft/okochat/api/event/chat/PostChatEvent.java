package net.okocraft.okochat.api.event.chat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface PostChatEvent extends ChatEvent {

    @NotNull Component getFormattedMessage();

    @NotNull @Unmodifiable Collection<Sender> getRecipients();

}
