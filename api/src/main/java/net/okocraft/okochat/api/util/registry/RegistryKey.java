package net.okocraft.okochat.api.util.registry;

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class RegistryKey<K, V> {

    private static final Set<Key> CREATED_KEYS = ConcurrentHashMap.newKeySet();

    public static <K, V> RegistryKey<K, V> create(@NotNull Key key) {
        Objects.requireNonNull(key);
        if (CREATED_KEYS.add(key)) {
            return new RegistryKey<>(key);
        } else {
            throw new IllegalArgumentException("The key '" + key + "' is already created.");
        }
    }

    private final Key key;

    private RegistryKey(@NotNull Key key) {
        this.key = key;
    }

    public @NotNull Key getKey() {
        return this.key;
    }

    @SuppressWarnings("unchecked")
    public @NotNull Registry<K, V> tryCast(@NotNull Registry<?, ?> registry) {
        return (Registry<K, V>) registry;
    }
}
