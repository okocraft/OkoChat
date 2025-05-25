package net.okocraft.okochat.core.api;

import dev.siroshun.event4j.api.caller.EventCaller;
import dev.siroshun.event4j.api.listener.ListenerSubscriber;
import dev.siroshun.event4j.api.priority.Priority;
import dev.siroshun.event4j.tree.TreeEventService;
import net.kyori.adventure.key.Key;
import net.okocraft.okochat.api.OkoChat;
import net.okocraft.okochat.api.event.OkoChatEvent;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public class OkoChatAPI implements OkoChat {

    private final TreeEventService<Key, OkoChatEvent, Priority> eventService;

    public OkoChatAPI(TreeEventService<Key, OkoChatEvent, Priority> eventService) {
        this.eventService = eventService;
    }

    @Override
    public EventCaller<OkoChatEvent> eventCaller() {
        return this.eventService.caller();
    }

    @Override
    public ListenerSubscriber<Key, OkoChatEvent, Priority> listenerSubscriber() {
        return this.eventService.subscriber();
    }
}
