package net.okocraft.okochat.core.platform.provider;

import net.okocraft.okochat.core.member.ChannelMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface ChannelMemberProvider {

    @Nullable ChannelMember getByUniqueId(@NotNull UUID uuid);

    @Nullable ChannelMember getByName(@NotNull String name);

}
