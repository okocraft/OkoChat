package net.okocraft.okochat.bridge.paper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.okocraft.okochat.bridge.paper.listener.ChatListener;
import net.okocraft.okochat.bridge.paper.listener.LegacyChatListener;
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
        // TODO: remove this after legacy mode is dropped
        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            commands.registrar().register("reloadocb", (source, args) -> {
                if (source.getSender().isOp()) {
                    this.setup();
                    source.getSender().sendPlainMessage("OkoChatBridge has been reloaded.");
                }
            });
        });

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
        this.saveDefaultConfig();
        this.reloadConfig();
        boolean legacyMode = this.getConfig().getBoolean("legacy-mode");

        AffixProvider<Player> affixProvider;

        if (VaultIntegration.canIntegrate()) {
            affixProvider = VaultIntegration.createAffixProvider();
        } else if (LuckPermsIntegration.canIntegrate()) {
            affixProvider = LuckPermsIntegration.createAffixProvider(Player::getUniqueId);
        } else {
            affixProvider = AffixProvider.createVoid();
        }

        if (legacyMode) {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "lunachat:message");
            this.getServer().getPluginManager().registerEvents(new LegacyChatListener(affixProvider, this.getSLF4JLogger()), this);
            return;
        }

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, OkoChatProtocol.CHANNEL);
        PluginMessageSender pluginMessageSender = new PluginMessageSender(this, OkoChatProtocol.CHANNEL);
        this.getServer().getPluginManager().registerEvents(new ChatListener(affixProvider, pluginMessageSender, this.getSLF4JLogger()), this);
    }
}
