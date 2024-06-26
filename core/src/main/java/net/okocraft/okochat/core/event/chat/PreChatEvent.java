package net.okocraft.okochat.core.event.chat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PreChatEvent extends ChatEvent {

    @NotNull String getResultMessage();

    void setModifiedMessage(@Nullable String modifiedMessage);

}
