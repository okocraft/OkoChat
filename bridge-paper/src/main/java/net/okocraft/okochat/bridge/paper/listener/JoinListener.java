package net.okocraft.okochat.bridge.paper.listener;

import net.okocraft.okochat.bridge.paper.messaging.PluginMessageSender;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.bridge.protocol.SyncPlayerRequestData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public class JoinListener implements Listener {

    private final PluginMessageSender pluginMessageSender;

    public JoinListener(PluginMessageSender pluginMessageSender) {
        this.pluginMessageSender = pluginMessageSender;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        this.pluginMessageSender.send(event.getPlayer(), OkoChatProtocol.REQUEST_PLAYER_DATA_SYNC, new SyncPlayerRequestData(event.getPlayer().getUniqueId()));
    }
}
