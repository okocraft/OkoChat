package net.okocraft.okochat.core.util.registry;

import net.kyori.adventure.key.Key;
import net.okocraft.okochat.api.util.registry.Registry;
import net.okocraft.okochat.api.util.registry.RegistryAccess;
import net.okocraft.okochat.api.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

final class RegistryAccessImpl implements RegistryAccess {

    private final Map<Key, Registry<?, ?>> registryMap;

    RegistryAccessImpl(@NotNull Map<Key, Registry<?, ?>> registryMap) {
        this.registryMap = registryMap;
    }

    @Override
    public <K, V> @NotNull Registry<K, V> getOrThrow(@NotNull RegistryKey<K, V> key) {
        return this.getTypedRegistryByKeyOrThrow(key);
    }

    private <K, V> @NotNull Registry<K, V> getTypedRegistryByKeyOrThrow(@NotNull RegistryKey<K, V> key) {
        var raw = this.registryMap.get(key.getKey());

        if (raw == null) {
            throw new IllegalArgumentException("The registry that is keyed as " + key.getKey() + " does not exist.");
        }

        return key.tryCast(raw);
    }
}
