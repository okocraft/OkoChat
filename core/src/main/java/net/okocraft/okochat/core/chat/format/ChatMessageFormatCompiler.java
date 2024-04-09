package net.okocraft.okochat.core.chat.format;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentIteratorType;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.format.ChatMessageFormat;
import net.okocraft.okochat.api.chat.format.placeholder.Placeholder;
import net.okocraft.okochat.api.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class ChatMessageFormatCompiler<C extends ChatContext> {

    private static final char PLACEHOLDER_BRACKET = '%';
    private final Registry<String, Placeholder<C>> registry;

    public ChatMessageFormatCompiler(@NotNull Registry<String, Placeholder<C>> registry) {
        this.registry = registry;
    }

    public @NotNull ChatMessageFormat<C> compile(@NotNull Component format) {
        var result = new ArrayList<StyleInheritingPlaceholder<C>>();

        if (format instanceof TextComponent rootText) {
            var style = format.style();
            this.compile(rootText.content(), placeholder -> result.add(new StyleInheritingPlaceholder<>(placeholder, style)));
        }

        if (format.children().isEmpty()) {
            return new ChatMessageFormatImpl<>(Collections.unmodifiableList(result));
        }

        for (var element : format.iterable(ComponentIteratorType.DEPTH_FIRST)) {
            if (element == format) { // Ignore the root component.
                continue;
            }

            if (element instanceof TextComponent textComponent) {
                var style = element.style();
                this.compile(textComponent.content(), placeholder -> result.add(new StyleInheritingPlaceholder<>(placeholder, style)));
            }
        }

        return new ChatMessageFormatImpl<>(Collections.unmodifiableList(result));
    }

    private void compile(@NotNull String raw, @NotNull Consumer<Placeholder<C>> consumer) {
        boolean inPlaceholder = false;

        var textBuilder = new StringBuilder();

        for (int codePoint : raw.codePoints().toArray()) {
            if (codePoint == PLACEHOLDER_BRACKET) {
                if (inPlaceholder) {
                    var rawPlaceholder = textBuilder.toString();
                    var placeholder = this.registry.getOrNull(rawPlaceholder);

                    consumer.accept(
                            placeholder != null ?
                                    placeholder :
                                    Placeholder.string(
                                            textBuilder.insert(0, PLACEHOLDER_BRACKET)
                                                    .append(PLACEHOLDER_BRACKET).toString()
                                    )
                    );

                    textBuilder.setLength(0);
                } else {
                    var text = textBuilder.toString();

                    if (!text.isEmpty()) {
                        consumer.accept(Placeholder.string(text));
                        textBuilder.setLength(0);
                    }
                }

                inPlaceholder = !inPlaceholder;
            } else {
                textBuilder.appendCodePoint(codePoint);
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
        private void appendRendered(@NotNull TextComponent.Builder builder, @NotNull C context) {
            builder.append(this.placeholder.apply(context).applyFallbackStyle(this.style));
        }
    }

    private record ChatMessageFormatImpl<C extends ChatContext>(
            @NotNull List<StyleInheritingPlaceholder<C>> placeholders
    ) implements ChatMessageFormat<C> {
        @SuppressWarnings("ForLoopReplaceableByForEach")
        @Override
        public @NotNull Component render(@NotNull C context) {
            var builder = Component.text();

            for (int i = 0, size = this.placeholders.size(); i < size; i++) {
                this.placeholders.get(i).appendRendered(builder, context);
            }

            return builder.build();
        }
    }
}
