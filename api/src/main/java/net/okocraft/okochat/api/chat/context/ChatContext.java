package net.okocraft.okochat.api.chat.context;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public sealed interface ChatContext permits PrivateChatContext {

    Sender sender();

    @NotNull SenderContext senderContext();

    String message();

    interface SenderContext {

        String name();

        Component displayName();

        Component prefix();

        Component suffix();

        String serverName();

        String worldName();

        int blockX();

        int blockY();

        int blockZ();

    }
}
