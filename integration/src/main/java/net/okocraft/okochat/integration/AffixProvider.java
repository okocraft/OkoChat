package net.okocraft.okochat.integration;

import org.jetbrains.annotations.NotNullByDefault;

import java.util.UUID;

@NotNullByDefault
public interface AffixProvider<P> {

    static <P> AffixProvider<P> createVoid() {
        return new AffixProvider<>() {
            @Override
            public String getPrefix(P player) {
                return "";
            }

            @Override
            public String getPrefix(UUID uuid) {
                return "";
            }

            @Override
            public String getSuffix(P player) {
                return "";
            }

            @Override
            public String getSuffix(UUID uuid) {
                return "";
            }
        };
    }

    String getPrefix(P player);

    String getPrefix(UUID uuid);

    String getSuffix(P player);

    String getSuffix(UUID uuid);

}
