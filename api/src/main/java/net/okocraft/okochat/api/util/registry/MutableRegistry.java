package net.okocraft.okochat.api.util.registry;

import org.jetbrains.annotations.NotNull;

public interface MutableRegistry<K, V> extends Registry<K, V> {

    boolean register(@NotNull K key, @NotNull V value);

    boolean register(@NotNull K key, @NotNull V value, boolean overwrite);

    boolean unregister(@NotNull K key);

}
