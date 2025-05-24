package net.okocraft.okochat.api.chat.format;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface ChatMessageFormat<C extends ChatContext> {

    @NotNull Component render(@NotNull C context);

}
