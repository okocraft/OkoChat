package net.okocraft.okochat.api.util.registry;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Set;

public interface Registry<K, V> extends Iterable<KeyedValue<K, V>> {

    @NotNull RegistryKey<K, V> getKey();

    @NotNull V getOrThrow(@NotNull K key);

    @Nullable V getOrNull(@NotNull K key);

    @NotNull @UnmodifiableView Set<K> keys();

    @NotNull @UnmodifiableView Collection<KeyedValue<K, V>> values();

}
