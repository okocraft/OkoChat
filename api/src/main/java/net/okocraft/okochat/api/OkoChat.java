package net.okocraft.okochat.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

@NotNullByDefault
public interface OkoChat {

    /**
     * Gets the {@link OkoChat} API.
     * <p>
     * If the {@link OkoChat} is not initialized, this method throws {@link IllegalStateException}
     *
     * @return the {@link OkoChat} API
     * @throws IllegalStateException if the {@link OkoChat} is not initialized
     */
    static OkoChat api() throws IllegalStateException {
        if (OkoChatAPI.instance == null) {
            throw new IllegalStateException("OkoChat is not initialized");
        }
        return OkoChatAPI.instance;
    }

    /**
     * Gets the {@link Logger} for OkoChat.
     * <p>
     * This method is for internal usage.
     *
     * @return the {@link Logger} for OkoChat
     */
    @ApiStatus.Internal
    static Logger logger() {
        return OkoChatAPI.LOGGER;
    }
}
