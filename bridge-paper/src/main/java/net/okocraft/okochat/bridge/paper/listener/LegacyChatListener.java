package net.okocraft.okochat.bridge.paper.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.okocraft.okochat.bridge.paper.OkoChatBridgePaperPlugin;
import net.okocraft.okochat.integration.AffixProvider;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

@NotNullByDefault
public class LegacyChatListener implements Listener {

    private final AffixProvider<Player> affixProvider;
    private final Logger logger;

    public LegacyChatListener(AffixProvider<Player> affixProvider, Logger logger) {
        this.affixProvider = affixProvider;
        this.logger = logger;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String prefix = this.affixProvider.getPrefix(player);
        String suffix = this.affixProvider.getSuffix(player);
        Location location = player.getLocation();

        byte[] data;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream out = new DataOutputStream(baos) ) {
            out.writeUTF(player.getName());
            out.writeUTF(player.getDisplayName());
            out.writeUTF(prefix);
            out.writeUTF(suffix);
            out.writeUTF(String.format("%s,%d,%d,%d", player.getWorld().getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(PlainTextComponentSerializer.plainText().serialize(event.message()));
            data = baos.toByteArray();
        } catch (IOException e) {
            this.logger.error("Failed to write chat message", e);
            event.setCancelled(true);
            return;
        }

        player.sendPluginMessage(
                JavaPlugin.getPlugin(OkoChatBridgePaperPlugin.class),
                "lunachat:message",
                data
        );

        this.logger.info("{}: {}", player.getName(), PlainTextComponentSerializer.plainText().serialize(event.message()));

        event.setCancelled(true);
    }
}
