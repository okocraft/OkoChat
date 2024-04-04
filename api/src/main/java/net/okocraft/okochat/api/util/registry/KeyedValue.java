package net.okocraft.okochat.api.util.registry;

import org.jetbrains.annotations.NotNull;

public interface KeyedValue<K, V> {

    @NotNull K key();

    @NotNull V value();

}
