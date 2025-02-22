package net.okocraft.okochat.integration.luckperms;

import net.okocraft.okochat.integration.AffixProvider;
import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;
import java.util.function.Function;

@NotNullByDefault
public final class LuckPermsIntegration {

    public static boolean canIntegrate() {
        try {
            Class.forName("net.luckperms.api.LuckPermsProvider");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static <P> AffixProvider<P> createAffixProvider(Function<P, UUID> uuidFunction) {
        return new LuckPermsAffixProvider<>(uuidFunction);
    }

    private LuckPermsIntegration() {
        throw new UnsupportedOperationException();
    }
}
