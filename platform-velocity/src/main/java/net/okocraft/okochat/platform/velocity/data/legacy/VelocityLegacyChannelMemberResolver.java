package net.okocraft.okochat.platform.velocity.data.legacy;

import net.okocraft.okochat.core.data.legacy.LegacyChannelMemberResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class VelocityLegacyChannelMemberResolver implements LegacyChannelMemberResolver {

    @Override
    public @Nullable UUID resolveName(@NotNull String name) {
        return null; // FIXME: not implemented
    }
}
