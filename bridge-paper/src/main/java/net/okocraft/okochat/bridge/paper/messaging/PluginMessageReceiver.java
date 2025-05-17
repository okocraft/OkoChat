package net.okocraft.okochat.bridge.paper.messaging;

import net.okocraft.okochat.bridge.paper.sync.SyncedValues;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

@NotNullByDefault
public class PluginMessageReceiver implements PluginMessageListener, OkoChatProtocol.Listener {

    private final String channel;
    private final Logger logger;

    private final SyncedValues syncedValues;

    public PluginMessageReceiver(String channel, Logger logger, SyncedValues syncedValues) {
        this.channel = channel;
        this.logger = logger;
        this.syncedValues = syncedValues;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!this.channel.equals(channel)) {
            return;
        }

        this.processPluginMessage(player.getUniqueId(), message, this.logger);
    }
}
