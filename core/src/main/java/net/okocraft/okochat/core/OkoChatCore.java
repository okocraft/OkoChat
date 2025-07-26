package net.okocraft.okochat.core;

import dev.siroshun.event4j.api.priority.Priority;
import dev.siroshun.event4j.tree.TreeEventService;
import net.kyori.adventure.key.Key;
import net.okocraft.okochat.api.OkoChatAPIAccessor;
import net.okocraft.okochat.api.event.OkoChatEvent;
import net.okocraft.okochat.core.api.OkoChatAPI;
import net.okocraft.okochat.core.chat.hide.HideListHolder;
import net.okocraft.okochat.core.data.FileList;
import net.okocraft.okochat.core.platform.Platform;
import org.jetbrains.annotations.NotNull;

public final class OkoChatCore {

    private final TreeEventService<Key, OkoChatEvent, Priority> eventService =
            TreeEventService.factory()
                    .keyClass(Key.class)
                    .eventClass(OkoChatEvent.class)
                    .defaultOrder(Priority.NORMAL)
                    .create();

    private final Platform platform;
    private final FileList fileList;

    public OkoChatCore(@NotNull Platform platform) {
        OkoChatAPIAccessor.setLogger(platform.logger());
        this.platform = platform;
        this.fileList = new FileList(platform.dataDirectory());
    }

    public void enable() {
        HideListHolder hideListHolder = this.fileList.initializeHideListHolder();
        this.fileList.migrateLegacyHideListIfExists(hideListHolder, this.platform.legacyChannelMemberResolver());

        this.fileList.scheduleAutoSave(this.platform.scheduler());

        OkoChatAPIAccessor.setInstance(new OkoChatAPI(
                this.eventService,
                this.platform.recipientProvider(),
                hideListHolder
        ));
    }

    public void disable() {
        OkoChatAPIAccessor.setInstance(null);

        this.fileList.shutdownAutoSaveTask();
    }

    public @NotNull Platform platform() {
        return this.platform;
    }
}
