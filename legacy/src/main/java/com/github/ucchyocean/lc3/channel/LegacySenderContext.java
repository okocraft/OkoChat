package com.github.ucchyocean.lc3.channel;

import com.github.ucchyocean.lc3.member.ChannelMember;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.okocraft.okochat.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Objects;

@NotNullByDefault
public record LegacySenderContext(ChannelMember sender) implements ChatContext.SenderContext {
    @Override
    public String name() {
        return this.sender.name();
    }

    @Override
    public Component displayName() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.sender.getDisplayName());
    }

    @Override
    public Component prefix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.sender.getPrefix());
    }

    @Override
    public Component suffix() {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(this.sender.getSuffix());
    }

    @Override
    public String serverName() {
        return Objects.requireNonNullElse(this.sender.getServerName(), "");
    }

    @Override
    public String worldName() {
        return Objects.requireNonNullElse(this.sender.getWorldName(), "");
    }

    // Not supported
    @Override
    public int blockX() {
        return 0;
    }

    @Override
    public int blockY() {
        return 0;
    }

    @Override
    public int blockZ() {
        return 0;
    }
}
