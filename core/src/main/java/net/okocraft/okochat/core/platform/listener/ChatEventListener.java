package net.okocraft.okochat.core.platform.listener;

public abstract class ChatEventListener {

    protected Priority priority() {
        return Priority.NORMAL; // TODO: configurable
    }

    protected void processChatEvent() {
        // TODO: See BukkitEventListener and BungeeEventListener
    }

    protected enum Priority {
        LOWEST,
        LOW,
        NORMAL,
        HIGH,
        HIGHEST
    }
}
