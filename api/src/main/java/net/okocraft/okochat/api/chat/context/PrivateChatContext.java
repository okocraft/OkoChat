package net.okocraft.okochat.api.chat.context;

import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;

public non-sealed interface PrivateChatContext extends ChatContext {

    @NotNull Sender target();

}
