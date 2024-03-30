package net.okocraft.okochat.core.event.channel;

import net.okocraft.okochat.core.channel.Channel;
import net.okocraft.okochat.core.event.LunaChatEvent;
import org.jetbrains.annotations.NotNull;

public interface ChannelEvent extends LunaChatEvent {

    @NotNull Channel getChannel();

}
