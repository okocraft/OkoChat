package net.okocraft.okochat.api.permission;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record Permission(@NotNull String node, boolean defaultValue) {

    @Contract("_ -> new")
    public static @NotNull Permission defaultAllowed(String node) {
        return new Permission(node, true);
    }

    @Contract("_ -> new")
    public static @NotNull Permission op(String node) {
        return new Permission(node, false);
    }

}
