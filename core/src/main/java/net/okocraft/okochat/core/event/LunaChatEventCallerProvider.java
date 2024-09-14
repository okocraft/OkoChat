package net.okocraft.okochat.core.event;

import dev.siroshun.event4j.api.caller.EventCaller;
import net.okocraft.okochat.api.event.LunaChatEvent;
import org.jetbrains.annotations.NotNull;

/**
 * A provider to get {@link EventCaller}s for LunaChat.
 *
 * @param sync an {@link EventCaller} that processes event synchronous
 * @param async an {@link EventCaller} that processes event asynchronous
 */
public record LunaChatEventCallerProvider(
        @NotNull EventCaller<LunaChatEvent> sync,
        @NotNull EventCaller<LunaChatEvent> async
) {
}
