package net.okocraft.okochat.api;

import org.slf4j.Logger;

public final class OkoChatAPIAccessor {

    public static void setInstance(OkoChat instance) {
        OkoChatAPI.instance = instance;
    }

    public static void setLogger(Logger logger) {
        OkoChatAPI.LOGGER.setDelegate(logger);
    }
}
