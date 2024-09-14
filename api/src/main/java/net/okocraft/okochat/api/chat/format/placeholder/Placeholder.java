package net.okocraft.okochat.api.chat.format.placeholder;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.context.PrivateChatContext;
import net.okocraft.okochat.api.util.registry.RegistryKey;
import org.jetbrains.annotations.NotNull;

public interface Placeholder<C extends ChatContext> {

    RegistryKey<String, Placeholder<ChannelChatContext>> REGISTRY_FOR_CHANNEL = RegistryKey.create(Key.key("okochat:chat/placeholder/channel"));
    RegistryKey<String, Placeholder<PrivateChatContext>> REGISTRY_FOR_PRIVATE_CHAT = RegistryKey.create(Key.key("okochat:chat/placeholder/private_chat"));

    static <C extends ChatContext> @NotNull Placeholder<C> string(@NotNull String value) {
        return new Placeholders.ComponentPlaceholder<>(Component.text(value));
    }

    @NotNull Component apply(@NotNull C context);

}
