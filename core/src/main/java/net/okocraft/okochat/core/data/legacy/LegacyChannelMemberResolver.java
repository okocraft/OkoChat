package net.okocraft.okochat.core.data.legacy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface LegacyChannelMemberResolver {

    @Nullable UUID resolveName(@NotNull String name);

    default @Nullable UUID resolveMember(@NotNull LegacyChannelMember member) {
        return switch (member) {
            case LegacyChannelMember.LegacyChannelMemberUUID(UUID uuid) -> uuid;
            case LegacyChannelMember.LegacyChannelMemberName(String name) -> this.resolveName(name);
        };
    }
}
