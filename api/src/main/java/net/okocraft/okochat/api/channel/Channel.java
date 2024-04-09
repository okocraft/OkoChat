package net.okocraft.okochat.api.channel;

import net.okocraft.okochat.api.channel.member.MemberHolder;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.chat.format.ChatMessageFormat;
import net.okocraft.okochat.api.permission.Permission;
import org.jetbrains.annotations.NotNull;

public interface Channel {

    @NotNull String getName();

    @NotNull MemberHolder getMemberHolder();

    @NotNull ChatMessageFormat<ChannelChatContext> getChatMessageFormat(); // TODO: rename to getFormat

    void setFormat(ChatMessageFormat<ChannelChatContext> format);

    @NotNull Permission getSpeakPermission();

}
