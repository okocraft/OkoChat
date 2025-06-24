package net.okocraft.okochat.platform.velocity;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import net.okocraft.okochat.core.platform.Scheduler;
import org.jetbrains.annotations.NotNullByDefault;

import java.time.Duration;

@NotNullByDefault
public record VelocityScheduler(Object plugin, ProxyServer server) implements Scheduler {

    @Override
    public CancellableTask schedule(Runnable task, Duration delay, Duration interval) {
        ScheduledTask scheduled =
                this.server.getScheduler()
                        .buildTask(this.plugin, task)
                        .delay(delay)
                        .repeat(interval).
                        schedule();
        return scheduled::cancel;
    }

}
