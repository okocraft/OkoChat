package net.okocraft.okochat.bridge.paper.listener;

import net.okocraft.okochat.bridge.paper.OkoChatBridgePaperPlugin;
import net.okocraft.okochat.bridge.paper.messaging.PluginMessageSender;
import net.okocraft.okochat.bridge.paper.sync.SyncedValues;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.bridge.protocol.SyncPlayerRequestData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public class ConnectionListener implements Listener {

    private final PluginMessageSender pluginMessageSender;
    private final SyncedValues syncedValues;

    public ConnectionListener(PluginMessageSender pluginMessageSender, SyncedValues syncedValues) {
        this.pluginMessageSender = pluginMessageSender;
        this.syncedValues = syncedValues;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        event.getPlayer().getScheduler().runDelayed(
                JavaPlugin.getPlugin(OkoChatBridgePaperPlugin.class),
                ignored -> this.pluginMessageSender.send(event.getPlayer(), OkoChatProtocol.REQUEST_PLAYER_DATA_SYNC, new SyncPlayerRequestData(event.getPlayer().getUniqueId())),
                null,
                40L
        );
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        this.syncedValues.removeValuesByPlayer(event.getPlayer().getUniqueId());
    }
}
