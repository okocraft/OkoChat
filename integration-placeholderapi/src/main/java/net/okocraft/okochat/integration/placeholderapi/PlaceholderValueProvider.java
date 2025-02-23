package net.okocraft.okochat.integration.placeholderapi;

import org.bukkit.entity.Player;

public interface PlaceholderValueProvider {

    String getDefaultChannelByPlayer(Player player);

    String getDefaultChannelByPlayerName(String name);

}
