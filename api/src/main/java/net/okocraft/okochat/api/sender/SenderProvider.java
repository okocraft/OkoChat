package net.okocraft.okochat.api.sender;

import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface SenderProvider {

    @NotNull ConsoleSender getConsole();

    @NotNull Optional<PlayerSender> findPlayerByName(@NotNull String name);

    @NotNull Optional<PlayerSender> findPlayerByUUID(@NotNull UUID uuid);

}
