package net.okocraft.okochat.core.chat.processor;

import com.github.siroshun09.event4j.caller.EventCaller;
import net.okocraft.okochat.api.channel.Channel;
import net.okocraft.okochat.api.channel.member.status.MemberStatus;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.chat.converter.ChatConverter;
import net.okocraft.okochat.api.chat.ngword.DetectedNGWord;
import net.okocraft.okochat.api.chat.ngword.NGWord;
import net.okocraft.okochat.api.chat.result.ChannelChatResult;
import net.okocraft.okochat.api.event.LunaChatEvent;
import net.okocraft.okochat.api.event.chat.channelchat.ChannelFindEvent;
import net.okocraft.okochat.api.event.chat.channelchat.PostChannelChatEvent;
import net.okocraft.okochat.api.event.chat.channelchat.PreChannelChatEvent;
import net.okocraft.okochat.api.identity.Identified;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.util.either.Either;
import net.okocraft.okochat.core.chat.context.ChannelChatContexts;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;
import java.util.regex.Pattern;

public record ChannelChatProcessor(
        @Nullable String globalChannelMarker,
        @Nullable Channel globalChannel,
        @Nullable Pattern quickChatSeparatorPattern,
        @NotNull Function<String, Channel> channelAccessor,
        @NotNull Function<Sender, Channel> defaultChannelProvider,
        @NotNull EventCaller<? super LunaChatEvent> eventCaller,
        @NotNull Function<Sender, ChatConverter> converterAccessor,
        @NotNull String disableChatConvertMarker,
        @NotNull Collection<NGWord> ngWords
) implements ChatProcessor<ProcessContext.ChannelChat, ChannelChatContext, ChannelChatResult> {

    @Override
    public @NotNull ChannelChatResult process(@NotNull ProcessContext.ChannelChat context) {
        return this.findChannel(context)
                .flatMap(this::callPreChannelChatEvent)
                .flatMap(this::checkPermission)
                .flatMap(this::checkMuted)
                .map(this::convertChatMessageIfNeeded)
                .flatMap(this::detectNGWords)
                .map(this::formatChannelChat)
                .map(this::collectRecipients)
                .map(this::sendMessage)
                .map(this::callPostChannelChatEvent)
                .fold(Function.identity(), this::success);
    }

    @Override
    public @NotNull ChannelChatResult onNonMaskableNGWordDetected(@NotNull ChannelChatContext context, @NotNull DetectedNGWord detectedNGWord) {
        return new ChannelChatResult.NGWord(context.sender(), context.message(), context.channel(), detectedNGWord);
    }

    @Override
    public @NotNull ChannelChatContext createContextWithNewMessage(@NotNull ChannelChatContext context, @NotNull String newMessage) {
        return new ChannelChatContexts.Preparing(context.sender(), newMessage, context.channel());
    }

    private @NotNull Either<ChannelChatResult, ChannelChatContext> findChannel(@NotNull ProcessContext.ChannelChat processContext) {
        return Either.<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>>left(processContext)
                .flatMapLeft(this::useSpecifiedChannelIfPossible)
                .flatMapLeft(this::findGlobalChatMarker)
                .flatMapLeft(this::findQuickChannelMarker)
                .flatMapLeft(this::findDefaultChannel)
                .flatMapLeft(this::fallbackToGlobalChannel)
                .mapLeft(data -> (ChannelChatResult) new ChannelChatResult.ChannelNotFound(data.sender(), data.message(), null))
                .flatMap(either -> either.isRight() ? this.callChannelFindEvent(processContext, either.right()) : either);
    }

    private @NotNull Either<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>> useSpecifiedChannelIfPossible(@NotNull ProcessContext.ChannelChat context) {
        return context.channel() != null ? Either.right(Either.right(new ChannelChatContexts.Preparing(context.sender(), context.message(), context.channel()))) : Either.left(context);
    }

    private @NotNull Either<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>> findGlobalChatMarker(ProcessContext.ChannelChat processContext) {
        if (this.globalChannelMarker == null || this.globalChannel == null) {
            return Either.left(processContext);
        }

        int markerLength = this.globalChannelMarker.length();
        var message = processContext.message();

        return markerLength != 0 && markerLength < message.length() && message.startsWith(this.globalChannelMarker) ?
                Either.right(channelFound(processContext.sender(), message.substring(markerLength), this.globalChannel)) :
                Either.left(processContext);
    }

    private @NotNull Either<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>> findQuickChannelMarker(@NotNull ProcessContext.ChannelChat processContext) {
        if (this.quickChatSeparatorPattern == null) {
            return Either.left(processContext);
        }

        var message = processContext.message();
        var parts = this.quickChatSeparatorPattern.split(message, 2);

        if (parts.length == 2 && !parts[1].isEmpty()) {
            var channel = this.channelAccessor.apply(parts[0]);
            return Either.right(
                    channel != null ?
                            Either.right(new ChannelChatContexts.Preparing(processContext.sender(), parts[1], channel)) :
                            Either.left(new ChannelChatResult.ChannelNotFound(processContext.sender(), parts[1], parts[0]))
            );
        } else {
            return Either.left(processContext);
        }
    }

    private @NotNull Either<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>> findDefaultChannel(@NotNull ProcessContext.ChannelChat processContext) {
        var defaultChannel = this.defaultChannelProvider.apply(processContext.sender());
        return defaultChannel != null ?
                Either.right(channelFound(processContext.sender(), processContext.message(), defaultChannel)) :
                Either.left(processContext);
    }

    private @NotNull Either<ProcessContext.ChannelChat, Either<ChannelChatResult, ChannelChatContext>> fallbackToGlobalChannel(@NotNull ProcessContext.ChannelChat processContext) {
        return this.globalChannel != null ?
                Either.right(Either.right(new ChannelChatContexts.Preparing(processContext.sender(), processContext.message(), this.globalChannel))) :
                Either.left(processContext);
    }

    private @NotNull Either<ChannelChatResult, ChannelChatContext> callChannelFindEvent(@NotNull ProcessContext.ChannelChat processContext, @NotNull ChannelChatContext context) {
        var event = new ChannelFindEvent(context.channel(), context.sender(), processContext.message(), context.message());
        this.eventCaller.call(event);
        return event.isCancelled() ?
                Either.left(new ChannelChatResult.ChannelNotFound(context.sender(), processContext.message(), null)) :
                Either.right(new ChannelChatContexts.Preparing(event.getSender(), event.getResultChatMessage(), event.getResultChannel()));
    }

    private static @NotNull Either<ChannelChatResult, ChannelChatContext> channelFound(@NotNull Sender sender, @NotNull String message, @NotNull Channel channel) {
        return Either.right(new ChannelChatContexts.Preparing(sender, message, channel));
    }

    private @NotNull Either<ChannelChatResult, ChannelChatContext> callPreChannelChatEvent(@NotNull ChannelChatContext context) {
        var event = new PreChannelChatEvent(context.channel(), context.sender(), context.message());
        this.eventCaller.call(event);
        return event.isCancelled() ?
                Either.left(new ChannelChatResult.EventCancelled(event.getSender(), event.getOriginalMessage(), event.getChannel())) :
                Either.right(new ChannelChatContexts.Preparing(event.getSender(), event.getResultMessage(), event.getChannel()));
    }

    private @NotNull Either<ChannelChatResult, ChannelChatContext> checkPermission(@NotNull ChannelChatContext context) {
        var permission = context.channel().getSpeakPermission();
        return context.sender().hasPermission(permission.node(), permission.defaultValue()) ?
                Either.right(context) :
                Either.left(new ChannelChatResult.NoPermission(context.sender(), context.message(), context.channel(), permission));
    }

    private @NotNull Either<ChannelChatResult, ChannelChatContext> checkMuted(@NotNull ChannelChatContext context) {
        return context.sender() instanceof Identified identified && context.channel().getMemberHolder().getStatus(identified.identity()) instanceof MemberStatus.Muted ?
                Either.left(new ChannelChatResult.Muted(context.sender(), context.message(), context.channel())) :
                Either.right(context);
    }

    private @NotNull ChannelChatContexts.Formatted formatChannelChat(@NotNull ChannelChatContext context) {
        return new ChannelChatContexts.Formatted(context.sender(), context.message(), context.channel(), context.channel().getChatMessageFormat().render(context));
    }

    private @NotNull ChannelChatContexts.Ready collectRecipients(@NotNull ChannelChatContexts.Formatted context) {
        // TODO
        return new ChannelChatContexts.Ready(context.sender(), context.message(), context.channel(), context.formattedMessage(), Collections.emptyList());
    }

    private @NotNull ChannelChatContexts.Ready sendMessage(@NotNull ChannelChatContexts.Ready context) {
        context.recipients().forEach(recipient -> recipient.sendMessage(context.formattedMessage())); // TODO: event?
        return context;
    }

    private @NotNull ChannelChatContexts.Ready callPostChannelChatEvent(@NotNull ChannelChatContexts.Ready context) {
        this.eventCaller.call(new PostChannelChatEvent(context.channel(), context.sender(), context.message(), context.formattedMessage(), context.recipients()));
        return context;
    }

    private @NotNull ChannelChatResult success(@NotNull ChannelChatContexts.Ready context) {
        return new ChannelChatResult.Success(context.sender(), context.message(), context.channel(), context.formattedMessage(), context.recipients());
    }
}
