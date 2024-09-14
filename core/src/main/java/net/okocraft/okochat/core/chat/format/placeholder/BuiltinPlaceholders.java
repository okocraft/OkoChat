package net.okocraft.okochat.core.chat.format.placeholder;

import net.kyori.adventure.text.Component;
import net.okocraft.okochat.api.chat.context.ChannelChatContext;
import net.okocraft.okochat.api.chat.context.ChatContext;
import net.okocraft.okochat.api.chat.context.PrivateChatContext;
import net.okocraft.okochat.api.chat.format.placeholder.Placeholder;
import net.okocraft.okochat.api.sender.PlayerSender;
import net.okocraft.okochat.api.sender.Sender;
import net.okocraft.okochat.api.util.registry.MutableRegistry;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class BuiltinPlaceholders {

    private static <C extends ChatContext> void registerSharedPlaceholders(@NotNull MutableRegistry<String, Placeholder<C>> registry) {
        registerPlayerPlaceholders(registry, "sender_", ChatContext::sender);

        registry.register("date", ignored -> Component.text(DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDate.now())));
        registry.register("time", ignored -> Component.text(DateTimeFormatter.ISO_LOCAL_TIME.format(LocalTime.now())));
    }

    public static void forChannelChat(@NotNull MutableRegistry<String, Placeholder<ChannelChatContext>> registry) {
        registerSharedPlaceholders(registry);

        registry.register("channel_name", context -> Component.text(context.channel().getName()));
    }

    public static void forPrivateChat(@NotNull MutableRegistry<String, Placeholder<PrivateChatContext>> registry) {
        registerSharedPlaceholders(registry);

        registerPlayerPlaceholders(registry, "target_", PrivateChatContext::target);
    }

    private static <C extends ChatContext> void registerPlayerPlaceholders(@NotNull MutableRegistry<String, Placeholder<C>> registry, @NotNull String keyPrefix, @NotNull Function<C, Sender> toSender) {
        registry.register(keyPrefix + "name", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getName())));
        registry.register(keyPrefix + "display_name", createPlayerPlaceholder(toSender, PlayerSender::getDisplayName));
        registry.register(keyPrefix + "prefix", createPlayerPlaceholder(toSender, PlayerSender::getPrefix));
        registry.register(keyPrefix + "suffix", createPlayerPlaceholder(toSender, PlayerSender::getSuffix));
        registry.register(keyPrefix + "server", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getServerName())));
        registry.register(keyPrefix + "world", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getWorldName())));
        registry.register(keyPrefix + "block_x", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getBlockX())));
        registry.register(keyPrefix + "block_y", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getBlockY())));
        registry.register(keyPrefix + "block_z", createPlayerPlaceholder(toSender, sender -> Component.text(sender.getBlockZ())));
    }

    private static <C extends ChatContext> @NotNull Placeholder<C> createPlayerPlaceholder(@NotNull Function<C, Sender> toSender, @NotNull Function<PlayerSender, Component> toComponent) {
        return context -> {
            var sender = toSender.apply(context);
            if (sender instanceof PlayerSender playerSender) {
                return toComponent.apply(playerSender);
            }
            return Component.empty();
        };
    }
}
