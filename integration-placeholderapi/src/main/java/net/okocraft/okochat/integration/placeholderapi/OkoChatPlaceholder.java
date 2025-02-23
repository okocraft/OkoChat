package net.okocraft.okochat.integration.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

class OkoChatPlaceholder extends PlaceholderExpansion implements RegisteredPlaceholders {

    private final String version;
    private final PlaceholderValueProvider provider;

    OkoChatPlaceholder(String version, PlaceholderValueProvider provider) {
        this.version = version;
        this.provider = provider;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "okochat";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Siroshun09";
    }

    @Override
    public @NotNull String getVersion() {
        return this.version;
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        int idx = params.indexOf("_");
        if (idx == -1) {
            return "";
        }

        return switch (params.substring(0, idx)) {
            case "default_channel" ->this.provider.getDefaultChannelByPlayer(player);
            case "version" -> this.version;
            default -> "";
        };
    }
}
