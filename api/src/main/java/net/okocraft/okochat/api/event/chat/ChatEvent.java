package net.okocraft.okochat.api.event.chat;

import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.event.LunaChatEvent;
import org.jetbrains.annotations.NotNull;

public interface ChatEvent extends LunaChatEvent {

    @NotNull Sender getSender();

    @NotNull String getOriginalMessage();

}
