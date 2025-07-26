package net.okocraft.okochat.integration.placeholderapi;

import java.util.UUID;

public interface PlaceholderValueProvider {

    String getDefaultChannelByPlayer(UUID uuid);

}
