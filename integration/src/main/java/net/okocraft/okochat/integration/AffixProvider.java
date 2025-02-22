package net.okocraft.okochat.integration;

import org.jetbrains.annotations.NotNullByDefault;

@NotNullByDefault
public interface AffixProvider<P> {

    static <P> AffixProvider<P> createVoid() {
        return new AffixProvider<>() {
            @Override
            public String getPrefix(P player) {
                return "";
            }

            @Override
            public String getSuffix(P player) {
                return "";
            }
        };
    }

    String getPrefix(P player);

    String getSuffix(P player);

}
