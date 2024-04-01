package net.okocraft.okochat.api.identity;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

final class Identities {

    record Player(@NotNull UUID uuid) implements PlayerIdentity {
        @Override
        public @NotNull UUID getUniqueId() {
            return this.uuid;
        }
    }

    record Channel(@NotNull String name) implements ChannelIdentity {
        @Override
        public @NotNull String getName() {
            return this.name;
        }
    }

}
