package net.okocraft.okochat.bridge.paper;

import net.okocraft.okochat.bridge.paper.listener.ChatListener;
import net.okocraft.okochat.bridge.paper.listener.ConnectionListener;
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
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

@NotNullByDefault
public class OkoChatBridgePaperPlugin extends JavaPlugin {

    private @Nullable RegisteredPlaceholders registeredPlaceholders = null;

    @Override
    public void onEnable() {
        SyncedValues syncedValues = new SyncedValues();
        PluginMessageSender pluginMessageSender = new PluginMessageSender(this, OkoChatProtocol.CHANNEL);
        PluginMessageReceiver pluginMessageReceiver = new PluginMessageReceiver(OkoChatProtocol.CHANNEL, this.getSLF4JLogger(), syncedValues);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, OkoChatProtocol.CHANNEL);
        this.setupChatBridge(pluginMessageSender);

        boolean needSyncValues;
        needSyncValues = this.setupPlaceholderAPIIntegration(syncedValues);

        if (needSyncValues) {
            this.getServer().getPluginManager().registerEvents(new ConnectionListener(pluginMessageSender, syncedValues), this);
            this.getServer().getMessenger().registerIncomingPluginChannel(this, OkoChatProtocol.CHANNEL, pluginMessageReceiver);
        }
    }

    @Override
    public void onDisable() {
        if (this.registeredPlaceholders != null) {
            this.registeredPlaceholders.unregister();
        }
    }

    private void setupChatBridge(PluginMessageSender pluginMessageSender) {
        AffixProvider<Player> affixProvider;

        if (VaultIntegration.canIntegrate()) {
            affixProvider = VaultIntegration.createAffixProvider();
        } else if (LuckPermsIntegration.canIntegrate()) {
            affixProvider = LuckPermsIntegration.createAffixProvider(Player::getUniqueId);
        } else {
            affixProvider = AffixProvider.createVoid();
        }

        this.getServer().getPluginManager().registerEvents(new ChatListener(affixProvider, pluginMessageSender, this.getSLF4JLogger()), this);

        if (affixProvider.getProviderName().isEmpty()) {
            this.getSLF4JLogger().info("Enabled OkoChat bridge");
        } else {
            this.getSLF4JLogger().info("Enabled OkoChat bridge with {} integration", affixProvider.getProviderName());
        }
    }

    private boolean setupPlaceholderAPIIntegration(SyncedValues syncedValues) {
        if (!PlaceholderAPIIntegration.canIntegrate()) {
            return false;
        }

        this.registeredPlaceholders = PlaceholderAPIIntegration.registerPlaceholders(this.getPluginMeta().getVersion(), syncedValues);

        this.getSLF4JLogger().info("Enabled PlaceholderAPI integration");
        return true;
    }
}
