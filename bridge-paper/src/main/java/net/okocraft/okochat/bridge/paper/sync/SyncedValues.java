package net.okocraft.okochat.bridge.paper.sync;

import net.okocraft.okochat.integration.placeholderapi.PlaceholderValueProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SyncedValues implements PlaceholderValueProvider {

    private final Map<UUID, String> defaultChannelNameMap = new ConcurrentHashMap<>();

    @Override
    public String getDefaultChannelByPlayer(Player player) {
        return this.defaultChannelNameMap.getOrDefault(player.getUniqueId(), "");
    }

    @Override
    public String getDefaultChannelByPlayerName(String name) {
        UUID uuid = Bukkit.getPlayerUniqueId(name);
        if (uuid == null) {
            return "";
        }
        return this.defaultChannelNameMap.getOrDefault(uuid, "");
    }

    public void updateDefaultChannelName(UUID uuid, String channelName) {
        this.defaultChannelNameMap.put(uuid, channelName);
    }
}
