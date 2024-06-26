package net.okocraft.okochat.api.sender;

import net.okocraft.okochat.api.identity.Identified;
import net.okocraft.okochat.api.identity.Identity;
import net.okocraft.okochat.api.identity.PlayerIdentity;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface PlayerSender extends Sender, Identified {

    @NotNull UUID getUniqueId();

    @Override
    default @NotNull PlayerIdentity identity() {
        return Identity.player(this.getUniqueId());
    }
}
