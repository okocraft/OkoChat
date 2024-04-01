package net.okocraft.okochat.api.identity;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

public interface Identity extends Identified {

    @Contract("_ -> new")
    static @NotNull PlayerIdentity player(@NotNull UUID uuid) {
        return new Identities.Player(Objects.requireNonNull(uuid));
    }

    @Contract("_ -> new")
    static @NotNull ChannelIdentity channel(@NotNull String name) {
        return new Identities.Channel(Objects.requireNonNull(name));
    }

    @Override
    default @NotNull Identity identity() {
        return this;
    }

}
