package net.okocraft.okochat.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.SubstituteLogger;

public final class OkoChatLogger {

    private static final SubstituteLogger LOGGER = new SubstituteLogger("OkoChat", null, true);

    static {
        try {
            Class.forName("org.junit.jupiter.api.Assertions");
            LOGGER.setDelegate(LoggerFactory.getLogger(OkoChatLogger.class));
        } catch (ClassNotFoundException ignored) {
        }
    }

    /**
     * Gets OkoChat's {@link Logger}.
     *
     * @return OkoChat's {@link Logger}.
     */
    public static Logger logger() {
        return LOGGER;
    }

    private OkoChatLogger() {
        throw new UnsupportedOperationException();
    }
}
