package net.okocraft.okochat.integration.vault;

import net.milkbowl.vault.chat.Chat;
import net.okocraft.okochat.integration.AffixProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.Objects;
import java.util.UUID;

@NotNullByDefault
class VaultAffixProvider implements AffixProvider<Player> {

    private final Chat chat;

    VaultAffixProvider(Chat chat) {
        this.chat = chat;
    }

    @Override
    public String getProviderName() {
        return "Vault";
    }

    @Override
    public String getPrefix(Player player) {
        return Objects.requireNonNullElse(this.chat.getPlayerPrefix(player), "");
    }

    @Override
    public String getPrefix(UUID uuid) {
        var player = Bukkit.getPlayer(uuid);
        return player == null ? "" : this.getPrefix(player);
    }

    @Override
    public String getSuffix(Player player) {
        return Objects.requireNonNullElse(this.chat.getPlayerSuffix(player), "");
    }

    @Override
    public String getSuffix(UUID uuid) {
        var player = Bukkit.getPlayer(uuid);
        return player == null ? "" : this.getSuffix(player);
    }
}
