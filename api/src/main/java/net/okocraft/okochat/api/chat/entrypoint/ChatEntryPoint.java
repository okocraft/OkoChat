package net.okocraft.okochat.api.chat.entrypoint;

import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.chat.result.ChannelChatResult;
import net.okocraft.okochat.api.chat.result.PrivateChatResult;
import net.okocraft.okochat.api.sender.Sender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ChatEntryPoint {

    default @NotNull ChannelChatResult processChannelChat(@NotNull Sender sender, @NotNull String message) {
        return this.processChannelChat(sender, message, null);
    }

    @NotNull ChannelChatResult processChannelChat(@NotNull Sender sender, @NotNull String message, @Nullable Channel channel);

    @NotNull PrivateChatResult processPrivateChat(@NotNull Sender sender, @NotNull String message, @NotNull Sender target);

}
