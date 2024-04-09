package net.okocraft.okochat.api.chat.context;

import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;

public sealed interface ChatContext permits ChannelChatContext, PrivateChatContext {

    @NotNull Sender sender();

    @NotNull String message();

}
