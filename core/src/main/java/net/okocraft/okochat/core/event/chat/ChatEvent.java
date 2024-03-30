package net.okocraft.okochat.core.event.chat;

import net.okocraft.okochat.core.event.LunaChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;

public interface ChatEvent extends LunaChatEvent {

    @NotNull ChannelMember getSender();

    @NotNull String getOriginalMessage();

}
