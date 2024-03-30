package net.okocraft.okochat.core.event;

import com.github.siroshun09.event4j.caller.AsyncEventCaller;
import com.github.siroshun09.event4j.listener.ListenerBase;
import com.github.siroshun09.event4j.listener.SubscribedListener;
import com.github.siroshun09.event4j.priority.Priority;
import com.github.siroshun09.event4j.simple.EventServiceProvider;
import com.github.siroshun09.event4j.subscriber.EventSubscriber;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public final class LunaChatEventService implements AsyncEventCaller<LunaChatEvent> {

    private final EventServiceProvider<Key, LunaChatEvent, Priority> delegate;

    public LunaChatEventService(@NotNull EventServiceProvider<Key, LunaChatEvent, Priority> delegate) {
        this.delegate = delegate;
    }

    @Override
    public <T extends LunaChatEvent> void call(@NotNull T event) {
        this.delegate.caller().call(event);
    }

    @Override
    public <T extends LunaChatEvent> void callAsync(@NotNull T event) {
        this.delegate.asyncCaller().callAsync(event);
    }

    @Override
    public <T extends LunaChatEvent> void callAsync(@NotNull T event, @Nullable Consumer<? super T> consumer) {
        this.delegate.asyncCaller().callAsync(event, consumer);
    }

    public @NotNull <T extends LunaChatEvent> EventSubscriber<Key, T, Priority> subscriber(@NotNull Class<T> eventClass) {
        return this.delegate.subscriber(eventClass);
    }

    public @NotNull Collection<SubscribedListener<Key, ? extends LunaChatEvent, Priority>> subscribeAll(@NotNull Iterable<ListenerBase<Key, ? extends LunaChatEvent, Priority>> iterable) {
        return this.delegate.subscribeAll(iterable);
    }

    public void unsubscribeAll(@NotNull Collection<? extends SubscribedListener<Key, ? extends LunaChatEvent, Priority>> collection) {
        this.delegate.unsubscribeAll(collection);
    }

    public void unsubscribeByKey(@NotNull Key key) {
        this.delegate.unsubscribeByKey(key);
    }
}
