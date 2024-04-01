package net.okocraft.okochat.api.identity;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerIdentity extends Identity {

    @NotNull UUID getUniqueId();

}
