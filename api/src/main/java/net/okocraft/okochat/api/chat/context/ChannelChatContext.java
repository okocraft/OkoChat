package net.okocraft.okochat.api.chat.context;

import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public non-sealed interface ChannelChatContext extends ChatContext {

    String channelName();

}
