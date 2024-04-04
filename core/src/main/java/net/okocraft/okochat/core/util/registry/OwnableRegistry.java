package net.okocraft.okochat.core.util.registry;

import net.okocraft.okochat.api.util.registry.KeyedValue;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

final class OwnableRegistry<K, V> {

    private static final VarHandle OWNER_HANDLE;

    static {
        try {
            OWNER_HANDLE = MethodHandles.lookup().findVarHandle(OwnableRegistry.class, "owner", RegistryOwner.class);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final Map<K, KeyedValue<K, V>> backing;
    private final Map<K, KeyedValue<K, V>> view;
    @SuppressWarnings("unused")
    private volatile RegistryOwner owner;

    OwnableRegistry() {
        this.backing = new ConcurrentHashMap<>();
        this.view = Collections.unmodifiableMap(this.backing);
    }

    OwnableRegistry(@NotNull Map<K, KeyedValue<K, V>> source) {
        this.backing = new ConcurrentHashMap<>(source);
        this.view = Collections.unmodifiableMap(this.backing);
    }

    void acquireOwner(@NotNull RegistryOwner owner) {
        if (!OWNER_HANDLE.compareAndSet(this, null, owner)) {
            throw new IllegalStateException("There is another owner.");
        }
    }

    void releaseOwner(@NotNull RegistryOwner owner) {
        if (!OWNER_HANDLE.compareAndSet(this, owner, null)) {
            throw new IllegalStateException("This registry is not owned.");
        }
    }

    void transferOwner(@NotNull RegistryOwner current, @NotNull RegistryOwner newOwner) {
        if (!OWNER_HANDLE.compareAndSet(this, current, newOwner)) {
            throw new IllegalStateException("This registry is not owned.");
        }
    }

    boolean register(@NotNull RegistryOwner expectedOwner, @NotNull K key, @NotNull V value, boolean overwrite) {
        if (OWNER_HANDLE.get(this) != expectedOwner) {
            throw new IllegalStateException("This registry is not owned.");
        }

        var registering = new OwnableRegistry.RegisteredValue<>(key, value);

        if (overwrite) {
            return !registering.equals(this.backing.put(key, registering)); // Registered value is updated
        } else {
            return this.backing.putIfAbsent(key, registering) == null; // No value registered previously
        }
    }

    boolean unregister(@NotNull RegistryOwner expectedOwner, @NotNull K key) {
        if (OWNER_HANDLE.get(this) == expectedOwner) {
            return this.backing.remove(key) != null;
        } else {
            throw new IllegalStateException("This registry is not owned.");
        }
    }

    @NotNull Map<K, KeyedValue<K, V>> getViewAccess(@NotNull RegistryOwner expectedOwner) {
        if (OWNER_HANDLE.get(this) == expectedOwner) {
            return this.view;
        } else {
            throw new IllegalStateException("This registry is not owned.");
        }
    }

    public @NotNull Iterator<KeyedValue<K, V>> iterator(@NotNull RegistryOwner expectedOwner) {
        if (OWNER_HANDLE.get(this) == expectedOwner) {
            return this.view.values().iterator();
        } else {
            throw new IllegalStateException("This registry is not owned.");
        }
    }

    private record RegisteredValue<K, V>(@NotNull K key, @NotNull V value) implements KeyedValue<K, V> {
    }
}
