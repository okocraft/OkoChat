package net.okocraft.okochat.api.recipient;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.List;

@NotNullByDefault
record RecipientImpl(Identity identity, Audience audience) implements Recipient {

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return List.of(this.audience);
    }

}
