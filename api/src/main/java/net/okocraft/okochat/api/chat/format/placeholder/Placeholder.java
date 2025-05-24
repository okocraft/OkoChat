package net.okocraft.okochat.api.chat.format.placeholder;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

public interface Placeholder<C extends ChatContext> {

    static <C extends ChatContext> @NotNull Placeholder<C> string(@NotNull String value) {
        return new Placeholders.ComponentPlaceholder<>(Component.text(value));
    }

    @NotNull Component apply(@NotNull C context);

}
