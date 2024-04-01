package net.okocraft.okochat.api.sender;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;

public interface Sender {

    String getName();

    void sendMessage(@NotNull Component message);

    default boolean hasPermission(@NotNull String node) {
        return this.hasPermission(node, false);
    }

    default boolean hasPermission(@NotNull String node, boolean defaultValue) {
        return this.getPermissionValue(node).toBooleanOrElse(defaultValue);
    }

    TriState getPermissionValue(@NotNull String node);

}
