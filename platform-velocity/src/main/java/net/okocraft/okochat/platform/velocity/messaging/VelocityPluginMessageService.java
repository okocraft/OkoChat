package net.okocraft.okochat.platform.velocity.messaging;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import net.kyori.adventure.identity.Identified;
import net.okocraft.okochat.api.OkoChat;
import net.okocraft.okochat.api.sender.ConsoleSender;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.core.platform.PluginMessageService;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

import static net.okocraft.okochat.api.OkoChat.logger;

@NotNullByDefault
public class VelocityPluginMessageService implements PluginMessageService {

    private static final ChannelIdentifier CHANNEL_IDENTIFIER = MinecraftChannelIdentifier.from(OkoChatProtocol.CHANNEL);

    private final Object plugin;
    private final ProxyServer server;

    public VelocityPluginMessageService(Object plugin, ProxyServer server) {
        this.plugin = plugin;
        this.server = server;
    }

    @Override
    public void registerListener(OkoChatProtocol.Listener listener) {
        this.server.getChannelRegistrar().register(CHANNEL_IDENTIFIER);
        this.server.getEventManager().register(this.plugin, new PluginMessageListener(listener));
    }

    @Override
    public <T> void send(Identified sender, OkoChatProtocol.MessageType<T> type, T data) {
        UUID uuid = sender.identity().uuid();
        this.server.getPlayer(uuid).flatMap(Player::getCurrentServer).ifPresentOrElse(
                server -> {
                    try {
                        server.sendPluginMessage(CHANNEL_IDENTIFIER, OkoChatProtocol.encodeData(type, data));
                    } catch (Exception e) {
                        logger().warn("Failed to send plugin message: {}", data, e);
                    }
                },
                () -> logger().warn("Cannot send plugin message because the sender is not online: {}", uuid)
        );
    }

    private record PluginMessageListener(OkoChatProtocol.Listener listener) {

        @Subscribe
        public void onPluginMessageReceived(PluginMessageEvent event) {
            if (!event.getIdentifier().equals(CHANNEL_IDENTIFIER)) {
                return;
            }

            event.setResult(PluginMessageEvent.ForwardResult.handled()); // For discarding messages from clients

            if (event.getSource() instanceof ServerConnection) {
                this.listener.processPluginMessage(
                        event.getTarget() instanceof Player receiver ? receiver.getUniqueId() : ConsoleSender.CONSOLE_UUID,
                        event.getData(),
                        OkoChat.logger()
                );
            }
        }
    }
}
