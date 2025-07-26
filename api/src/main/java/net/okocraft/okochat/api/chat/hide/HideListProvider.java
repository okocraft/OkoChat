package net.okocraft.okochat.api.chat.hide;

import net.kyori.adventure.identity.Identified;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public interface HideListProvider {

    default HideList getByIdentified(Identified identified) {
        return this.getByUUID(identified.identity().uuid());
    }

    HideList getByUUID(UUID uuid);

}
