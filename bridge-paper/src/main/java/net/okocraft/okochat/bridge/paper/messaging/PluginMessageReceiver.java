package net.okocraft.okochat.bridge.paper.messaging;

import net.okocraft.okochat.bridge.paper.sync.SyncedValues;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.bridge.protocol.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

@NotNullByDefault
public class PluginMessageReceiver implements PluginMessageListener {

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

        try (ByteArrayInputStream bin = new ByteArrayInputStream(message);
             DataInputStream in = new DataInputStream(bin)) {
            int version = OkoChatProtocol.readVersion(in);
            if (version == 1) {
                this.processV1(in);
            } else {
                this.logger.error("Unknown protocol version received: {}", version);
            }
        } catch (Exception e) {
            this.logger.error("Failed to read plugin message from {}", channel, e);
        }
    }

    private void processV1(DataInputStream in) throws Exception {
        byte type = OkoChatProtocol.readMessageType(in);

        if (type == OkoChatProtocol.SYNC_PLAYER_DATA.identity()) {
            PlayerData data = OkoChatProtocol.SYNC_PLAYER_DATA.reader().read(in);
            this.syncedValues.updateDefaultChannelName(data.uuid(), data.defaultChannelName());
        } else {
            this.logger.error("Unknown protocol type received: {}", type);
        }
    }
}
