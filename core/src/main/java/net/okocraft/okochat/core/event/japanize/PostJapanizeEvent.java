package net.okocraft.okochat.core.event.japanize;

import net.okocraft.okochat.core.event.LunaChatEvent;
import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("ClassCanBeRecord")
public class PostJapanizeEvent implements LunaChatEvent {

    private final ChannelMember member;
    private final String originalMessage;
    private final String japanizedMessage;

    public PostJapanizeEvent(@NotNull ChannelMember member,
                             @NotNull String originalMessage, @NotNull String japanizedMessage) {
        this.member = member;
        this.originalMessage = originalMessage;
        this.japanizedMessage = japanizedMessage;
    }

    public @NotNull ChannelMember getMember() {
        return this.member;
    }

    public @NotNull String getOriginalMessage() {
        return this.originalMessage;
    }

    public @NotNull String getJapanizedMessage() {
        return this.japanizedMessage;
    }
}
