package net.okocraft.okochat.api.util.registry;

import org.jetbrains.annotations.NotNull;

public interface RegistryAccess {

    <K, V> @NotNull Registry<K, V> getOrThrow(@NotNull RegistryKey<K, V> key);

}
