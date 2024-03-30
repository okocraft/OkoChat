package net.okocraft.okochat.core.platform.provider;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface UserProvider {

    @Nullable UUID lookupUuid(@NotNull String name);

    @Nullable String lookupName(@NotNull UUID uuid);

}
