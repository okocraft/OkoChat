package net.okocraft.okochat.bridge.paper;

import net.okocraft.okochat.bridge.paper.listener.ChatListener;
import net.okocraft.okochat.bridge.paper.messaging.PluginMessageReceiver;
import net.okocraft.okochat.bridge.paper.messaging.PluginMessageSender;
import net.okocraft.okochat.bridge.paper.sync.SyncedValues;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.integration.AffixProvider;
import net.okocraft.okochat.integration.luckperms.LuckPermsIntegration;
import net.okocraft.okochat.integration.placeholderapi.PlaceholderAPIIntegration;
import net.okocraft.okochat.integration.placeholderapi.RegisteredPlaceholders;
import net.okocraft.okochat.integration.vault.VaultIntegration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

@NotNullByDefault
public class OkoChatBridgePaperPlugin extends JavaPlugin {

    private final SyncedValues syncedValues = new SyncedValues();
    private @Nullable RegisteredPlaceholders registeredPlaceholders = null;

    @Override
    public void onEnable() {
        this.setup();

        this.getServer().getMessenger().registerIncomingPluginChannel(this, OkoChatProtocol.CHANNEL, new PluginMessageReceiver(OkoChatProtocol.CHANNEL, this.getSLF4JLogger(), this.syncedValues));

        if (PlaceholderAPIIntegration.canIntegrate()) {
            this.registeredPlaceholders = PlaceholderAPIIntegration.registerPlaceholders(
                    this.getPluginMeta().getVersion(),
                    this.syncedValues
            );
        }
    }

    @Override
    public void onDisable() {
        if (this.registeredPlaceholders != null) {
            this.registeredPlaceholders.unregister();
        }
    }

    private void setup() {
        HandlerList.unregisterAll(this);

        AffixProvider<Player> affixProvider;

        if (VaultIntegration.canIntegrate()) {
            affixProvider = VaultIntegration.createAffixProvider();
        } else if (LuckPermsIntegration.canIntegrate()) {
            affixProvider = LuckPermsIntegration.createAffixProvider(Player::getUniqueId);
        } else {
            affixProvider = AffixProvider.createVoid();
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, OkoChatProtocol.CHANNEL);
        PluginMessageSender pluginMessageSender = new PluginMessageSender(this, OkoChatProtocol.CHANNEL);
        this.getServer().getPluginManager().registerEvents(new ChatListener(affixProvider, pluginMessageSender, this.getSLF4JLogger()), this);
    }
}
