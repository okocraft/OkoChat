package net.okocraft.okochat.api;

import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;

final class OkoChatAPI {

    static final SubstituteLogger LOGGER = new SubstituteLogger("OkoChat", null, true);

    static {
        try {
            Class.forName("org.junit.jupiter.api.Assertions");
            LOGGER.setDelegate(LoggerFactory.getLogger("OkoChat"));
        } catch (ClassNotFoundException ignored) {
        }
    }

    static OkoChat instance;

    private OkoChatAPI() {
        throw new UnsupportedOperationException();
    }
}
