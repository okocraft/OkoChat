package net.okocraft.okochat.api.expirable;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

public interface Expirable {

    Instant PERMANENT = Instant.MAX;

    @NotNull Instant expiration();

    default boolean isExpired() {
        return this.isExpiredAt(Instant.now());
    }

    default boolean isExpiredAt(@NotNull Instant time) {
        return this.expiration().isBefore(time);
    }

    default boolean isPermanent() {
        return this.expiration().equals(PERMANENT);
    }

}
