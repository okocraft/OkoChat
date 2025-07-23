package com.github.ucchyocean.lc3.channel;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.sender.Sender;

public record LegacyChannelChatContext(String channelName, Sender sender, SenderContext senderContext, String message, Component formattedMessage) implements ChannelChatContext {
}
