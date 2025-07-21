package net.okocraft.okochat.core.chat.format;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.format.ChatMessageFormat;
import net.okocraft.okochat.api.chat.format.placeholder.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public final class ChatMessageFormatCompiler<C extends ChatContext> {

    private static final char PLACEHOLDER_BRACKET = '%';
    private final Map<String, Placeholder<C>> registry;

    public ChatMessageFormatCompiler(@NotNull Map<String, Placeholder<C>> registry) {
        this.registry = registry;
    }

    public @NotNull ChatMessageFormat<C> compile(@NotNull Component format) {
        List<StyleInheritingPlaceholder<C>> result = new ArrayList<>();

        if (format instanceof TextComponent rootText) {
            Style style = format.style();
            this.compile(rootText.content(), placeholder -> result.add(new StyleInheritingPlaceholder<>(placeholder, style)));
        }

        if (format.children().isEmpty()) {
            return new ChatMessageFormatImpl<>(Collections.unmodifiableList(result));
        }

        for (Component element : format.iterable(ComponentIteratorType.DEPTH_FIRST)) {
            if (element == format) { // Ignore the root component.
                continue;
            }

            if (element instanceof TextComponent textComponent) {
                Style style = element.style();
                this.compile(textComponent.content(), placeholder -> result.add(new StyleInheritingPlaceholder<>(placeholder, style)));
            }
        }

        return new ChatMessageFormatImpl<>(Collections.unmodifiableList(result));
    }

    private void compile(@NotNull String raw, @NotNull Consumer<Placeholder<C>> consumer) {
        boolean inPlaceholder = false;

        StringBuilder textBuilder = new StringBuilder();

        for (int codePoint : raw.codePoints().toArray()) {
            if (codePoint == PLACEHOLDER_BRACKET) {
                String text = textBuilder.toString();
                if (!text.isEmpty()) {
                    consumer.accept(Placeholder.string(inPlaceholder ? PLACEHOLDER_BRACKET + text : text));
                    textBuilder.setLength(0);
                }
                inPlaceholder = true;
                continue;
            }

            textBuilder.appendCodePoint(codePoint);

            if (inPlaceholder) {
                String key = textBuilder.toString();
                Placeholder<C> placeholder = this.registry.get(key);
                if (placeholder != null) {
                    consumer.accept(placeholder);
                    textBuilder.setLength(0);
                    inPlaceholder = false;
                }
            }
        }

        if (!textBuilder.isEmpty()) {
            if (inPlaceholder) {
                consumer.accept(Placeholder.string(textBuilder.insert(0, PLACEHOLDER_BRACKET).toString()));
            } else {
                consumer.accept(Placeholder.string(textBuilder.toString()));
            }
        }
    }

    private record StyleInheritingPlaceholder<C extends ChatContext>(@NotNull Placeholder<? super C> placeholder,
                                                                     @NotNull Style style) {
        private @NotNull Component render(@NotNull C context) {
            return this.placeholder.apply(context).applyFallbackStyle(this.style);
        }
    }

    private record ChatMessageFormatImpl<C extends ChatContext>(
            @NotNull List<StyleInheritingPlaceholder<C>> placeholders
    ) implements ChatMessageFormat<C> {
        @SuppressWarnings("ForLoopReplaceableByForEach")
        @Override
        public @NotNull Component render(@NotNull C context) {
            TextComponent.Builder builder = Component.text();

            for (int i = 0, size = this.placeholders.size(); i < size; i++) {
                builder.append(this.placeholders.get(i).render(context));
            }

            return builder.build();
        }
    }
}
