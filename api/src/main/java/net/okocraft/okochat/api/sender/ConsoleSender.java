package net.okocraft.okochat.api.sender;


import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.util.TriState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public interface ConsoleSender extends Sender {

    UUID CONSOLE_UUID = new UUID(0, 0);

    String CONSOLE_NAME = "Console";

    Identity CONSOLE_IDENTITY = Identity.identity(CONSOLE_UUID);

    @Override
    default UUID uuid() {
        return CONSOLE_UUID;
    }

    @Override
    default String name() {
        return CONSOLE_NAME;
    }

    @Override
    default boolean hasPermission(String permissionNode) {
        return true;
    }

    @Override
    default TriState getPermissionValue(String permissionNode) {
        return TriState.TRUE;
    }

    @Override
    default @NotNull Identity identity() {
        return CONSOLE_IDENTITY;
    }
}
