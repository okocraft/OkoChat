package net.okocraft.okochat.core.util.registry;

import net.kyori.adventure.key.Key;
import net.okocraft.okochat.api.util.registry.MutableRegistry;
import net.okocraft.okochat.api.util.registry.Registry;
import net.okocraft.okochat.api.util.registry.RegistryAccess;
import net.okocraft.okochat.api.util.registry.RegistryKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class RegistryAccessBuilder {

    @Contract(" -> new")
    public static @NotNull RegistryAccessBuilder builder() {
        return new RegistryAccessBuilder();
    }

    private final Map<Key, Registry<?, ?>> registryMap = new HashMap<>();

    private RegistryAccessBuilder() {
    }

    @Contract("_, _ -> this")
    public <K, V> @NotNull RegistryAccessBuilder addRegistry(@NotNull RegistryKey<K, V> key, @NotNull Consumer<? super MutableRegistry<K, V>> consumer) {
        var registry = new SimpleRegistry<>(key, new OwnableRegistry<>());
        this.registryMap.put(key.getKey(), registry);
        consumer.accept(registry);
        return this;
    }

    public @NotNull RegistryAccess build() {
        return new RegistryAccessImpl(this.registryMap);
    }
}
