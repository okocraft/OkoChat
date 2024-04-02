package net.okocraft.okochat.api.event.chat.privatechat;

import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.event.chat.ChatEvent;
import org.jetbrains.annotations.NotNull;

public interface PrivateChatEvent extends ChatEvent {

    @NotNull Sender getTarget();

}
