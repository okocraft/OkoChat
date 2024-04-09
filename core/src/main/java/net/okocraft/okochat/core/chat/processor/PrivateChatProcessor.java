package net.okocraft.okochat.core.chat.processor;

import com.github.siroshun09.event4j.caller.EventCaller;
import net.okocraft.okochat.api.chat.context.PrivateChatContext;
import net.okocraft.okochat.api.chat.format.ChatMessageFormat;
import net.okocraft.okochat.api.chat.converter.ChatConverter;
import net.okocraft.okochat.api.event.LunaChatEvent;
import net.okocraft.okochat.api.event.chat.privatechat.PostPrivateChatEvent;
import net.okocraft.okochat.api.event.chat.privatechat.PrePrivateChatEvent;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.util.either.Either;
import net.okocraft.okochat.core.chat.context.PrivateChatContexts;
import net.okocraft.okochat.api.chat.result.PrivateChatResult;
import net.okocraft.okochat.api.chat.ngword.DetectedNGWord;
import net.okocraft.okochat.api.chat.ngword.NGWord;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public record PrivateChatProcessor(
        @NotNull EventCaller<? super LunaChatEvent> eventCaller,
        @NotNull ChatMessageFormat<PrivateChatContext> chatMessageFormat,
        @NotNull Function<Sender, ChatConverter> converterAccessor,
        @NotNull String disableChatConvertMarker,
        @NotNull Collection<NGWord> ngWords
) implements ChatProcessor<ProcessContext.PrivateChat, PrivateChatContext, PrivateChatResult> {

    @Override
    public @NotNull PrivateChatResult process(@NotNull ProcessContext.PrivateChat context) {
        return this.toPrivateChatContext(context)
                .flatMap(this::callPrePrivateChatEvent)
                .flatMap(this::checkMuted)
                .map(this::convertChatMessageIfNeeded)
                .flatMap(this::detectNGWords)
                .map(this::formatPrivateChat)
                .map(this::sendMessage)
                .map(this::callPostPrivateChatEvent)
                .fold(Function.identity(), this::success);
    }

    @Override
    public @NotNull PrivateChatResult onNonMaskableNGWordDetected(@NotNull PrivateChatContext context, @NotNull DetectedNGWord detectedNGWord) {
        return new PrivateChatResult.NGWord(context.sender(), context.message(), context.target(), detectedNGWord);
    }

    @Override
    public @NotNull PrivateChatContext createContextWithNewMessage(@NotNull PrivateChatContext context, @NotNull String newMessage) {
        return new PrivateChatContexts.Preparing(context.sender(), newMessage, context.target());
    }

    private @NotNull Either<PrivateChatResult, PrivateChatContext> toPrivateChatContext(@NotNull ProcessContext.PrivateChat processContext) {
        return Either.right(new PrivateChatContexts.Preparing(processContext.sender(), processContext.message(), processContext.target()));
    }

    private @NotNull Either<PrivateChatResult, PrivateChatContext> callPrePrivateChatEvent(@NotNull PrivateChatContext context) {
        var event = new PrePrivateChatEvent(context.sender(), context.target(), context.message());
        this.eventCaller.call(event);
        return event.isCancelled() ?
                Either.left(new PrivateChatResult.EventCancelled(event.getSender(), event.getOriginalMessage(), event.getTarget())) :
                Either.right(new PrivateChatContexts.Preparing(event.getSender(), event.getResultMessage(), event.getTarget()));
    }

    private @NotNull Either<PrivateChatResult, PrivateChatContext> checkMuted(@NotNull PrivateChatContext context) { // TODO
        return Either.right(context);
    }

    private @NotNull PrivateChatContexts.Ready formatPrivateChat(@NotNull PrivateChatContext context) {
        return new PrivateChatContexts.Ready(context.sender(), context.message(), context.target(), this.chatMessageFormat.render(context));
    }

    private @NotNull PrivateChatContexts.Ready callPostPrivateChatEvent(@NotNull PrivateChatContexts.Ready context) {
        this.eventCaller.call(new PostPrivateChatEvent(context.sender(), context.target(), context.message(), context.formattedMessage()));
        return context;
    }

    private @NotNull PrivateChatContexts.Ready sendMessage(@NotNull PrivateChatContexts.Ready context) {
        context.sender().sendMessage(context.formattedMessage());
        context.target().sendMessage(context.formattedMessage());
        return context;
    }

    private @NotNull PrivateChatResult success(@NotNull PrivateChatContexts.Ready context) {
        return new PrivateChatResult.Success(context.sender(), context.message(), context.target(), context.formattedMessage());
    }
}
