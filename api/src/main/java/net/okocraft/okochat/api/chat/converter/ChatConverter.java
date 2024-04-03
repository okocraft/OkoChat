package net.okocraft.okochat.api.chat.converter;

import net.kyori.adventure.key.Key;
import net.okocraft.okochat.api.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public interface ChatConverter {

    RegistryKey<Key, ChatConverter> REGISTRY_KEY = RegistryKey.create(Key.key("okochat:chat/converter"));

    ChatConverter NOOP = message -> message;

    @NotNull String convert(@NotNull String message);

}
