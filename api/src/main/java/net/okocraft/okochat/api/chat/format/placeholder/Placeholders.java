package net.okocraft.okochat.api.chat.format.placeholder;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

final class Placeholders {

    record ComponentPlaceholder<C extends ChatContext>(@NotNull Component component) implements Placeholder<C> {
        @Override
        public @NotNull Component apply(@NotNull C context) {
            return this.component;
        }
    }

    private Placeholders() {
        throw new UnsupportedOperationException();
    }
}
