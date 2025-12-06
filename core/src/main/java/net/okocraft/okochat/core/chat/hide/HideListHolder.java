package net.okocraft.okochat.core.chat.hide;

import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import net.okocraft.okochat.api.chat.hide.HideList;
import net.okocraft.okochat.api.chat.hide.HideListProvider;
import net.okocraft.okochat.core.data.hide.HideEntry;
import net.okocraft.okochat.core.data.hide.HideListData;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@NotNullByDefault
public class HideListHolder implements HideListProvider {

    private final Set<HideEntry> hideEntries = ConcurrentHashMap.newKeySet();
    private final AtomicBoolean dirty = new AtomicBoolean(false);

    @Override
    public HideList getByUUID(UUID uuid) {
        Objects.requireNonNull(uuid);
        return new ReferenceHideList(uuid);
    }

    public boolean checkDirty() {
        return this.dirty.compareAndSet(true, false);
    }

    public HideListData exportToData() {
        Map<UUID, List<UUID>> map = new HashMap<>();
        for (HideEntry entry : this.hideEntries) {
            map.computeIfAbsent(entry.uuid(), ignored -> new ArrayList<>()).add(entry.hidden());
        }

        map.values().forEach(Collections::sort);

        return new HideListData(Collections.unmodifiableMap(map));
    }

    public void importFromData(HideListData data) {
        for (Map.Entry<UUID, List<UUID>> entry : data.hideListMap().entrySet()) {
            for (UUID hidden : entry.getValue()) {
                this.hideEntries.add(new HideEntry(entry.getKey(), hidden));
            }
        }
    }

    private class ReferenceHideList implements HideList {

        private final UUID uuid;

        private ReferenceHideList(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public boolean isHidden(Identified target) {
            return HideListHolder.this.hideEntries.contains(new HideEntry(this.uuid, target.identity().uuid()));
        }

        @Override
        public void hide(Identified target) {
            HideListHolder.this.hideEntries.add(new HideEntry(this.uuid, target.identity().uuid()));
            HideListHolder.this.dirty.set(true);
        }

        @Override
        public void unhide(Identified target) {
            HideListHolder.this.hideEntries.remove(new HideEntry(this.uuid, target.identity().uuid()));
            HideListHolder.this.dirty.set(true);
        }

        @Override
        public Stream<Identity> stream() {
            return HideListHolder.this.hideEntries.stream()
                    .filter(entry -> entry.uuid().equals(this.uuid))
                    .map(entry -> Identity.identity(entry.hidden()));
        }
    }
}
