package net.okocraft.okochat.core.chat.format.placeholder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.context.PrivateChatContext;
import net.okocraft.okochat.api.chat.format.placeholder.Placeholder;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

public class BuiltinPlaceholders {

    private static <C extends ChatContext> void registerSharedPlaceholders(@NotNull Map<String, Placeholder<C>> registry) {
        registerPlayerPlaceholders(registry, "sender_", ChatContext::senderContext);

        registry.put("date", ignored -> Component.text(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));
        registry.put("time", ignored -> Component.text(DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now())));
        registry.put("msg", ChatContext::formattedMessage);
    }


    public static void forChannelChat(@NotNull Map<String, Placeholder<ChannelChatContext>> registry) {
        registerSharedPlaceholders(registry);

        // backwards compatibility
        registry.put("ch", context -> withChannelJoinCommandClickEvent(Component.text(context.channelName()), context.channelName()));
        registry.put("displayname", context -> withTellCommandClickEvent(context.senderContext().displayName(), context.sender().name()));
        registry.put("username", context -> withTellCommandClickEvent(context.senderContext().displayName(), context.sender().name()));
        registry.put("player", context -> withTellCommandClickEvent(Component.text(context.sender().name()), context.sender().name()));
        registry.put("prefix", createPlayerPlaceholder(ChatContext::senderContext, ChatContext.SenderContext::prefix));
        registry.put("suffix", createPlayerPlaceholder(ChatContext::senderContext, ChatContext.SenderContext::suffix));
        registry.put("world", createPlayerPlaceholder(ChatContext::senderContext, sender -> Component.text(sender.worldName())));
        registry.put("server", createPlayerPlaceholder(ChatContext::senderContext, sender -> Component.text(sender.serverName())));
    }

    public static void forPrivateChat(@NotNull Map<String, Placeholder<PrivateChatContext>> registry) {
        registerSharedPlaceholders(registry);

        registerPlayerPlaceholders(registry, "target_", PrivateChatContext::targetContext);
    }

    private static <C extends ChatContext> void registerPlayerPlaceholders(@NotNull Map<String, Placeholder<C>> registry, @NotNull String keyPrefix, @NotNull Function<C, ChatContext.SenderContext> toContext) {
        registry.put(keyPrefix + "name", createPlayerPlaceholder(toContext, sender -> Component.text(sender.name())));
        registry.put(keyPrefix + "display_name", createPlayerPlaceholder(toContext, ChatContext.SenderContext::displayName));
        registry.put(keyPrefix + "prefix", createPlayerPlaceholder(toContext, ChatContext.SenderContext::prefix));
        registry.put(keyPrefix + "suffix", createPlayerPlaceholder(toContext, ChatContext.SenderContext::suffix));
        registry.put(keyPrefix + "server", createPlayerPlaceholder(toContext, sender -> Component.text(sender.serverName())));
        registry.put(keyPrefix + "world", createPlayerPlaceholder(toContext, sender -> Component.text(sender.worldName())));
        registry.put(keyPrefix + "block_x", createPlayerPlaceholder(toContext, sender -> Component.text(sender.blockX())));
        registry.put(keyPrefix + "block_y", createPlayerPlaceholder(toContext, sender -> Component.text(sender.blockY())));
        registry.put(keyPrefix + "block_z", createPlayerPlaceholder(toContext, sender -> Component.text(sender.blockZ())));
    }

    private static <C extends ChatContext> @NotNull Placeholder<C> createPlayerPlaceholder(@NotNull Function<C, ChatContext.SenderContext> toContext, @NotNull Function<ChatContext.SenderContext, Component> toComponent) {
        return context -> toComponent.apply(toContext.apply(context));
    }

    private static @NotNull Component withChannelJoinCommandClickEvent(@NotNull Component component, String channelName) {
        return component.clickEvent(ClickEvent.suggestCommand("/ch join " + channelName));
    }

    private static @NotNull Component withTellCommandClickEvent(@NotNull Component component, String playerName) {
        return component.clickEvent(ClickEvent.suggestCommand("/tell " + playerName));
    }
}
