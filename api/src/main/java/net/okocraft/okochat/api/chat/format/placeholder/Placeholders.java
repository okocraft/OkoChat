package net.okocraft.okochat.api.chat.format.placeholder;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChatContext;
import org.jetbrains.annotations.NotNull;

final class Placeholders {

    record StringPlaceholder<C extends ChatContext>(@NotNull String value) implements Placeholder<C> {
        @Override
        public @NotNull Component apply(@NotNull C context) {
            return Component.text(this.value);
        }
    }

    private Placeholders() {
        throw new UnsupportedOperationException();
    }
}
