package net.okocraft.okochat.core.platform;

import org.jetbrains.annotations.NotNullByDefault;

import java.time.Duration;

@NotNullByDefault
public interface Scheduler {

    CancellableTask schedule(Runnable task, Duration delay, Duration interval);

    interface CancellableTask {
        void cancel();
    }
}
