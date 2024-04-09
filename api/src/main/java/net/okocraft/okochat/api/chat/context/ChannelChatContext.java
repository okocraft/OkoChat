package net.okocraft.okochat.api.chat.context;

import net.okocraft.okochat.api.channel.Channel;
import org.jetbrains.annotations.NotNull;

public non-sealed interface ChannelChatContext extends ChatContext {

    @NotNull Channel channel();

}
