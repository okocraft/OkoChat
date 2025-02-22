package net.okocraft.okochat.integration.vault;

import net.milkbowl.vault.chat.Chat;
import net.okocraft.okochat.integration.AffixProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public final class VaultIntegration {

    public static boolean canIntegrate() {
        return Bukkit.getPluginManager().isPluginEnabled("Vault");
    }

    public static AffixProvider<Player> createAffixProvider() {
        var r = Bukkit.getServicesManager().getRegistration(Chat.class);

        if (r != null) {
            return new VaultAffixProvider(r.getProvider());
        } else {
            throw new IllegalStateException("Vault Chat service not found.");
        }
    }

    private VaultIntegration() {
        throw new UnsupportedOperationException();
    }
}
