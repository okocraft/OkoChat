package net.okocraft.okochat.integration.placeholderapi;

import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;

@NotNullByDefault
public class PlaceholderAPIIntegration {

    public static boolean canIntegrate() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    public static @Nullable RegisteredPlaceholders registerPlaceholders(String version, PlaceholderValueProvider provider) {
        var expansion = new OkoChatPlaceholder(version, provider);
        if (expansion.register()) {
            return expansion;
        }
        return null;
    }
}
