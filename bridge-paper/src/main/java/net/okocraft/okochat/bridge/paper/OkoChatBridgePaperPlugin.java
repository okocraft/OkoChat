package net.okocraft.okochat.bridge.paper;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.okocraft.okochat.bridge.paper.listener.ChatListener;
import net.okocraft.okochat.bridge.paper.listener.LegacyChatListener;
import net.okocraft.okochat.bridge.protocol.OkoChatProtocol;
import net.okocraft.okochat.integration.AffixProvider;
import net.okocraft.okochat.integration.luckperms.LuckPermsIntegration;
import net.okocraft.okochat.integration.vault.VaultIntegration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public class OkoChatBridgePaperPlugin extends JavaPlugin {

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
        } else {
            this.getServer().getMessenger().registerOutgoingPluginChannel(this, OkoChatProtocol.CHANNEL);
            this.getServer().getPluginManager().registerEvents(new ChatListener(affixProvider, this.getSLF4JLogger()), this);
        }
    }
}
