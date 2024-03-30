package net.okocraft.okochat.core.event.chat;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface PostChatEvent extends ChatEvent {

    @NotNull Component getFormattedMessage();

    @NotNull @Unmodifiable Collection<ChannelMember> getRecipients();

}
