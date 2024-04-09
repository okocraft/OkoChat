package net.okocraft.okochat.core.chat.processor;

import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.converter.ChatConverter;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.util.either.Either;
import net.okocraft.okochat.api.chat.ngword.DetectedNGWord;
import net.okocraft.okochat.api.chat.ngword.NGWord;
import net.okocraft.okochat.api.chat.ngword.NGWordOperationType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public interface ChatProcessor<PC extends ProcessContext, C extends ChatContext, R> {

    @NotNull R process(@NotNull PC chatData);

    @NotNull Function<Sender, ChatConverter> converterAccessor();

    @NotNull String disableChatConvertMarker();

    @NotNull Collection<NGWord> ngWords();

    @NotNull R onNonMaskableNGWordDetected(@NotNull C context, @NotNull DetectedNGWord detectedNGWord);

    @NotNull C createContextWithNewMessage(@NotNull C context, @NotNull String newMessage);

    default @NotNull C convertChatMessageIfNeeded(@NotNull C context) {
        var converter = this.converterAccessor().apply(context.sender());

        if (converter == ChatConverter.NOOP) {
            return context;
        }

        var disableMarker = this.disableChatConvertMarker();

        if (!disableMarker.isEmpty() && context.message().startsWith(disableMarker)) {
            return context.message().equals(disableMarker) ?
                    context :
                    this.createContextWithNewMessage(context, context.message().substring(disableMarker.length()));
        }

        return this.createContextWithNewMessage(context, converter.convert(context.message()));
    }

    default @NotNull Either<R, C> detectNGWords(@NotNull C context) {
        var currentMessage = context.message();
        boolean masked = false;

        for (var ngWord : this.ngWords()) {
            var result = ngWord.detect(currentMessage);
            if (result.isPresent()) {
                var detected = result.get();
                if (detected.ngWord().operationType() == NGWordOperationType.MASK) {
                    currentMessage = detected.maskedMessage().orElseThrow();
                    masked = true;
                } else {
                    return Either.left(this.onNonMaskableNGWordDetected(context, detected));
                }
            }
        }

        return Either.right(masked ? this.createContextWithNewMessage(context, currentMessage) : context);
    }
}
