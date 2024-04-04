package net.okocraft.okochat.core.util.registry;

import net.okocraft.okochat.api.util.registry.KeyedValue;
import net.okocraft.okochat.api.util.registry.MutableRegistry;
import net.okocraft.okochat.api.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

final class SimpleRegistry<K, V> implements MutableRegistry<K, V> {

    private final RegistryKey<K, V> key;
    private final OwnableRegistry<K, V> backing;
    private final RegistryOwner owner = new RegistryOwner(this);

    SimpleRegistry(@NotNull RegistryKey<K, V> key, @NotNull OwnableRegistry<K, V> backing) {
        this.key = key;
        this.backing = backing;
    }

    @Override
    public @NotNull RegistryKey<K, V> getKey() {
        return this.key;
    }

    @Override
    public @NotNull V getOrThrow(@NotNull K key) {
        var value = this.backing.getViewAccess(this.owner).get(key);
        if (value == null) {
            throw new IllegalArgumentException("The given key does not exist.");
        }
        return value.value();
    }

    @Override
    public @Nullable V getOrNull(@NotNull K key) {
        var value = this.backing.getViewAccess(this.owner).get(key);
        return value != null ? value.value() : null;
    }

    @Override
    public @NotNull @UnmodifiableView Set<K> keys() {
        return Collections.unmodifiableSet(this.backing.getViewAccess(this.owner).keySet());
    }

    @Override
    public @NotNull @UnmodifiableView Collection<KeyedValue<K, V>> values() {
        return Collections.unmodifiableCollection(this.backing.getViewAccess(this.owner).values());
    }

    @Override
    public @NotNull Iterator<KeyedValue<K, V>> iterator() {
        return this.backing.iterator(this.owner);
    }

    @Override
    public boolean register(@NotNull K key, @NotNull V value) {
        return this.register(key, value, false);
    }

    @Override
    public boolean register(@NotNull K key, @NotNull V value, boolean overwrite) {
        return this.backing.register(this.owner, key, value, overwrite);
    }

    @Override
    public boolean unregister(@NotNull K key) {
        return this.backing.unregister(this.owner, key);
    }
}
