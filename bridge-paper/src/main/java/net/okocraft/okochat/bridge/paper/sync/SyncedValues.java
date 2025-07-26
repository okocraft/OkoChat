package net.okocraft.okochat.bridge.paper.sync;

import net.okocraft.okochat.integration.placeholderapi.PlaceholderValueProvider;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SyncedValues implements PlaceholderValueProvider {

    private final Map<UUID, String> defaultChannelNameMap = new ConcurrentHashMap<>();

    @Override
    public String getDefaultChannelByPlayer(UUID uuid) {
        return this.defaultChannelNameMap.getOrDefault(uuid, "");
    }

    public void updateDefaultChannelName(UUID uuid, String channelName) {
        this.defaultChannelNameMap.put(uuid, channelName);
    }

    public void removeValuesByPlayer(UUID uuid) {
        this.defaultChannelNameMap.remove(uuid);
    }
}
