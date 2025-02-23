package net.okocraft.okochat.bridge.paper.messaging;

import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageRecipient;

public class PluginMessageSender {

    private final Plugin plugin;
    private final String channel;

    public PluginMessageSender(Plugin plugin, String channel) {
        this.plugin = plugin;
        this.channel = channel;
    }

    public <T> void send(PluginMessageRecipient recipient, OkoChatProtocol.MessageType<T> type, T data) {
        try {
            recipient.sendPluginMessage(this.plugin, this.channel, OkoChatProtocol.encodeData(type, data));
        } catch (Exception e) {
            this.plugin.getSLF4JLogger().error("Failed to send plugin message: {}", data, e);
        }
    }
}
