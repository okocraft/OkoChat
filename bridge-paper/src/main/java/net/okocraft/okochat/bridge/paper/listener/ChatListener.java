package net.okocraft.okochat.bridge.paper.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.okocraft.okochat.bridge.paper.OkoChatBridgePaperPlugin;
import net.okocraft.okochat.bridge.protocol.BlockPosition;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.bridge.protocol.ServerChatMessageData;
import net.okocraft.okochat.bridge.protocol.ServerSenderData;
import net.okocraft.okochat.integration.AffixProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

@NotNullByDefault
public class ChatListener implements Listener {

    private final AffixProvider<Player> affixProvider;
    private final Logger logger;

    public ChatListener(AffixProvider<Player> affixProvider, Logger logger) {
        this.affixProvider = affixProvider;
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String prefix = this.affixProvider.getPrefix(player);
        String suffix = this.affixProvider.getSuffix(player);
        Location location = player.getLocation();

        ServerChatMessageData messageData = new ServerChatMessageData(
                new ServerSenderData(
                        player.getUniqueId(),
                        player.getName(),
                        player.displayName(),
                        prefix,
                        suffix,
                        player.getWorld().getName(),
                        new BlockPosition(
                                location.getBlockX(),
                                location.getBlockY(),
                                location.getBlockZ()
                        )
                ),
                event.message()
        );

        byte[] data;
        try {
            data = OkoChatProtocol.encodeData(OkoChatProtocol.CHAT, messageData);
        } catch (Exception e) {
            this.logger.error("Failed to write chat message: {}", messageData, e);
            event.setCancelled(true);
            return;
        }

        player.sendPluginMessage(
                JavaPlugin.getPlugin(OkoChatBridgePaperPlugin.class),
                OkoChatProtocol.CHANNEL,
                data
        );

        this.logger.info("[{}] {}", player.getName(), event.message());
        event.setCancelled(true);
    }
}
