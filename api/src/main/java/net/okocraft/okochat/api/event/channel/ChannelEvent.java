package net.okocraft.okochat.api.event.channel;

import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.event.LunaChatEvent;
import org.jetbrains.annotations.NotNull;

public interface ChannelEvent extends LunaChatEvent {

    @NotNull Channel getChannel();

}
