package net.okocraft.okochat.api.chat.recipient;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.identity.Identified;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Recipient extends Identified, ForwardingAudience {

    @Contract(pure = true)
    static @NotNull Recipient nullRecipient() {
        return NullRecipient.INSTANCE;
    }

    @Contract("_, _ -> new")
    static @NotNull Recipient create(@NotNull Identity identity, @NotNull Audience audience) {
        return new RecipientImpl(identity, audience);
    }

    default boolean isNull() {
        return false;
    }

}
