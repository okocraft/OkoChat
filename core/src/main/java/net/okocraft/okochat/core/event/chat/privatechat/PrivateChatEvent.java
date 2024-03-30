package net.okocraft.okochat.core.event.chat.privatechat;

import net.okocraft.okochat.core.event.chat.ChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;

public interface PrivateChatEvent extends ChatEvent {

    @NotNull ChannelMember getTarget();

}
