package net.okocraft.okochat.api.recipient;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class NullRecipient implements Recipient {

    static final NullRecipient INSTANCE = new NullRecipient();

    private NullRecipient() {
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return List.of();
    }

    @Override
    public @NotNull Identity identity() {
        return Identity.nil();
    }

    @Override
    public boolean isNull() {
        return true;
    }
}
